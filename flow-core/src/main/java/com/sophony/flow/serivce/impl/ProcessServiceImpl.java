package com.sophony.flow.serivce.impl;

import com.sophony.flow.annotation.FLowLock;
import com.sophony.flow.api.reqDto.ApproveReqDto;
import com.sophony.flow.api.reqDto.RefuseReqDto;
import com.sophony.flow.api.reqDto.WithdrawReqDto;
import com.sophony.flow.api.respDto.ActProcessTaskRespDto;
import com.sophony.flow.api.respDto.ProcessRespDto;
import com.sophony.flow.common.AnnotationOpenUtils;
import com.sophony.flow.common.FlowNotify;
import com.sophony.flow.common.MethodLoader;
import com.sophony.flow.common.constant.ParamKey;
import com.sophony.flow.commons.BusParam;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.commons.annotation.*;
import com.sophony.flow.commons.constant.NotifyEnum;
import com.sophony.flow.commons.constant.ProcessOperationEnum;
import com.sophony.flow.commons.constant.ProcessStateEnum;
import com.sophony.flow.commons.constant.ProcessTaskStateEnum;
import com.sophony.flow.commons.model.IProcess;
import com.sophony.flow.event.FlowRegisterEvent;
import com.sophony.flow.mapping.ActProcdef;
import com.sophony.flow.mapping.ActProcess;
import com.sophony.flow.mapping.ActProcessTask;
import com.sophony.flow.mapping.ActTaskProcdef;
import com.sophony.flow.model.ProcessCommonModel;
import com.sophony.flow.serivce.IProcessService;
import com.sophony.flow.worker.common.BaseService;
import com.sophony.flow.worker.common.FlowBeanFactory;
import com.sophony.flow.worker.common.FlowClassLoader;
import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ActProcessServiceImpl
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 23:29
 */
@Service
public class ProcessServiceImpl extends BaseService implements IProcessService {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ApplicationEventPublisher publisher;

    @Resource
    AnnotationOpenUtils annotationOpenUtils;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public String start(String processNo, IProcess process) {
        ActProcdef actProcdef = new ActProcdef();
        String sql = "select * from "+ actProcdef.getTableName() + " where act_no = ?  and is_deleted = 0 and state = '1' limit 1";
        actProcdef = super.selectOne(sql, ActProcdef.class, new Object[]{ processNo });
        if(Objects.isNull(actProcdef)){
            throw new RuntimeException(processNo+": 当前流程模板编号不可用");
        }
        ActProcess actProcess = new ActProcess();
        actProcess.setActId(actProcdef.getId());
        actProcess.setStartTime(LocalDateTime.now());
        actProcess.setState(ProcessStateEnum.START.getName());
        if(Objects.nonNull(process)){
            actProcess.setClassName(process.getClass().getName());
        }
        //查询路程开始节点
        ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
        String sql2 = actTaskProcdef.getQuerySql() + " where is_deleted = 0 and (task_no = 'start' or  task_no = 'end') and process_fid = ? ";
        List<ActTaskProcdef> list = super.list(sql2, ActTaskProcdef.class, new Object[]{actProcdef.getId()});
        if(CollectionUtils.isEmpty(list)){
            throw new RuntimeException(processNo+": 当前流程模板没有任务节点");
        }

        if(list.size() > 2){
            throw new RuntimeException(processNo+": 当前流程模板有重复开始任务节点或结束任务节点");
        }

        if(list.size() < 2){
            throw new RuntimeException(processNo+": 当前流程模板没有开始任务节点或没有结束任务节点");
        }
        Set<String> keys = list.stream().map(it -> it.getTaskNo()).collect(Collectors.toSet());
        if(keys.size() < 2 && keys.contains("start")){
            throw new RuntimeException(processNo+": 当前流程模板没有结束任务节点");
        }else if(keys.size() < 2 && keys.contains("end")){
            throw new RuntimeException(processNo+": 当前流程模板没有开始任务节点");
        }
        if(StringUtils.equals(list.get(0).getTaskNo(), "start")){
            actTaskProcdef =   list.get(0);
        }else {
            actTaskProcdef =   list.get(1);
        }
        String processId = super.insert(actProcess);
        ActProcessTask processTask = new ActProcessTask();
        processTask.setProcessId(processId);
        processTask.setStartTime(LocalDateTime.now());
        processTask.setProcessfName(actProcdef.getActName());
        processTask.setSort(actTaskProcdef.getSort());
        processTask.setProcessfId(actProcdef.getId());
        processTask.setTaskfId(actTaskProcdef.getId());
        processTask.setContent("初始化流程");
        processTask.setState(ProcessTaskStateEnum.RUN.getName());
        processTask.setTagIds(actTaskProcdef.getTagIds());
        processTask.setTaskNo(actTaskProcdef.getTaskNo());
        processTask.setTaskName(actTaskProcdef.getTaskName());
        processTask.setVoucher("true");
        processTask.setVoucherCount(actTaskProcdef.getNextTaskIds().split(",").length);
        String taskId = super.insert(processTask);
        taskRecord(taskId, actTaskProcdef.getId(), "");
        processTaskRecord(taskId, processId);
        FlowNotify flowNotify = new FlowNotify();
        flowNotify.setNotifyEnum(NotifyEnum.START);
        flowNotify.setHook(process);
        flowNotify.setProcessId(processId);
        this.startNotify(flowNotify, process);
        return processId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String start(String processNo, Class<?> clazz) {
        ActProcdef actProcdef = new ActProcdef();
        String sql = "select * from "+ actProcdef.getTableName() + " where act_no = ?  and is_deleted = 0 and state = '1' limit 1";
        actProcdef = super.selectOne(sql, ActProcdef.class, new Object[]{ processNo });
        if(Objects.isNull(actProcdef)){
            throw new RuntimeException(processNo+": 当前流程模板编号不可用");
        }
        ActProcess actProcess = new ActProcess();
        actProcess.setActId(actProcdef.getId());
        actProcess.setStartTime(LocalDateTime.now());
        actProcess.setState(ProcessStateEnum.START.getName());
        if(Objects.nonNull(clazz)){
            actProcess.setClassName(clazz.getName());
        }
        //查询路程开始节点
        ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
        String sql2 = actTaskProcdef.getQuerySql() + " where is_deleted = 0 and (task_no = 'start' or  task_no = 'end') and process_fid = ? ";
        List<ActTaskProcdef> list = super.list(sql2, ActTaskProcdef.class, new Object[]{actProcdef.getId()});
        if(CollectionUtils.isEmpty(list)){
            throw new RuntimeException(processNo+": 当前流程模板没有任务节点");
        }

        if(list.size() > 2){
            throw new RuntimeException(processNo+": 当前流程模板有重复开始任务节点或结束任务节点");
        }

        if(list.size() < 2){
            throw new RuntimeException(processNo+": 当前流程模板没有开始任务节点或没有结束任务节点");
        }
        Set<String> keys = list.stream().map(it -> it.getTaskNo()).collect(Collectors.toSet());
        if(keys.size() < 2 && keys.contains("start")){
            throw new RuntimeException(processNo+": 当前流程模板没有结束任务节点");
        }else if(keys.size() < 2 && keys.contains("end")){
            throw new RuntimeException(processNo+": 当前流程模板没有开始任务节点");
        }
        if(StringUtils.equals(list.get(0).getTaskNo(), "start")){
            actTaskProcdef =   list.get(0);
        }else {
            actTaskProcdef =   list.get(1);
        }
        String processId = super.insert(actProcess);
        ActProcessTask processTask = new ActProcessTask();
        processTask.setProcessId(processId);
        processTask.setStartTime(LocalDateTime.now());
        processTask.setProcessfName(actProcdef.getActName());
        processTask.setSort(actTaskProcdef.getSort());
        processTask.setProcessfId(actProcdef.getId());
        processTask.setTaskfId(actTaskProcdef.getId());
        processTask.setContent("初始化流程");
        processTask.setState(ProcessTaskStateEnum.RUN.getName());
        processTask.setTagIds(actTaskProcdef.getTagIds());
        processTask.setTaskNo(actTaskProcdef.getTaskNo());
        processTask.setTaskName(actTaskProcdef.getTaskName());
        processTask.setVoucher("true");
        processTask.setVoucherCount(actTaskProcdef.getNextTaskIds().split(",").length);
        String taskId = super.insert(processTask);
        taskRecord(taskId, actTaskProcdef.getId(), "");
        processTaskRecord(taskId, processId);
        Object process = null;
        try {
            process = FlowClassLoader.flowClassLoader(clazz);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }finally {
            if(Objects.isNull(process)){
                throw new RuntimeException("流程开启失败");
            }
        }
        FlowNotify flowNotify = new FlowNotify();
        flowNotify.setNotifyEnum(NotifyEnum.START);
        flowNotify.setHook(process);
        flowNotify.setProcessId(processId);
        this.startNotify(flowNotify);
        return processId;
    }


    /**
     * 审核同意
     * @param approveReqDto
     * @return
     */
    @Override
    @FLowLock
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO approve(ApproveReqDto approveReqDto) {
        String processId = approveReqDto.getProcessId();
        ActProcess actProcess = super.getById(processId, ActProcess.class);
        if(Objects.isNull(actProcess)){
            return ResultDTO.failed("失败");
        }
        if(StringUtils.equals(actProcess.getState(), ProcessStateEnum.END.getName())){
            return  ResultDTO.failed("流程已经结束,无法审核");
        }

        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);

        ProcessCommonModel processCommonModel = new ProcessCommonModel(processId, approveReqDto.getOperation(), true);
        boolean audit = processCommonModel.isAudit();
        if(!audit){
            return ResultDTO.failed("没有审核权限");
        }
        boolean b = beforeNotify(processId, actProcess.getClassName(), approveReqDto.getOperation());
        if(!b){
            return ResultDTO.failed("业务条件校验不通过，审核终止");
        }
        TaskNode taskNode = processCommonModel.getTaskNode();
        ActProcessTask task = new ActProcessTask();
        task.setId(taskNode.getId());
        task.setContent(approveReqDto.getContent());
        task.setEndTime(LocalDateTime.now());
        task.setState(ProcessTaskStateEnum.FINISH.getName());
        User currentUser = getCurrentUser();
        if(Objects.nonNull(currentUser)){
            task.setAuditUser(currentUser.getUserName());
        }
        Optional.ofNullable(processCommonModel.getT()).ifPresent(it ->
                        task.setExpansionData(new LinkedHashMap(){{
                            put("key1", it);
                        }})
                );
        super.updateById(task);
        //校验下一层节点
        ActTaskProcdef actTaskProcdef = super.getById(taskNode.getTaskTemplateId(), ActTaskProcdef.class);
        String nextTaskIds = actTaskProcdef.getNextTaskIds();
        List<ActTaskProcdef> actTaskProcdefs = super.selectByIds(nextTaskIds, ActTaskProcdef.class);
        if(CollectionUtils.isEmpty(actTaskProcdefs)){
            return ResultDTO.failed(processId+": 没有完整流程，当前流程不可继续");
        }

        if(actTaskProcdefs.size() > 1){
            Set<String> collect = actTaskProcdefs.stream().map(it -> it.getTaskNo()).collect(Collectors.toSet());
            if(collect.contains("end")){
                return ResultDTO.failed(processId+": "+ actTaskProcdef.getTaskNo() +":拥有多个子节点任务节点不能直接关联结束节点");
            }

        }
        actTaskProcdefs = actTaskProcdefs.stream().filter(it -> {
            boolean f = false;
            String preTaskIds = it.getPreTaskIds();
            if (StringUtils.isEmpty(preTaskIds)) {
                return true;
            }
            if (!preTaskIds.contains(",")) {
                return true;
            }
            String voucherTemp = super.getById(taskNode.getId(), ActProcessTask.class).getVoucher();
            String preTaskSql = task.getQuerySql() + " where is_deleted = 0 and state = 'FINISH' and voucher = 'true' and  taskf_id in " + super.conditionByIn(preTaskIds, String.class) + " order by id ";
            List<ActProcessTask> list = super.list(preTaskSql, ActProcessTask.class);
            Set<String> fids = list.stream().map(item -> item.getTaskfId()).collect(Collectors.toSet());
            Map<String, Boolean> res = new LinkedHashMap<>();
            for (String id : preTaskIds.split(",")) {
                res.put(id, fids.contains(id));
            }
            f = conditionLoad(it.getCond(), res, "task:" + task.getId() + " 审核同意, 因生成子任务节点中断任务", voucherTemp);
            return f;
        }).collect(Collectors.toList());


        if(CollectionUtils.isEmpty(actTaskProcdefs)){
            //审核后回调
            Map<String, Object> map = afterParamDispose(taskNode.getId(), approveReqDto.getOtherParam());
            afterNotify(processId, actProcess.getClassName(), approveReqDto.getOperation(), map);
            return ResultDTO.success("成功");
        }
        String parentHistory = super.getById(task.getId(), ActProcessTask.class).getSelfHistory();
        if(StringUtils.equals(actTaskProcdefs.get(0).getTaskNo(), "end")){
            ActTaskProcdef end = actTaskProcdefs.get(0);
            ActProcessTask actProcessTask = new ActProcessTask();
            actProcessTask.setState(ProcessTaskStateEnum.FINISH.getName());
            actProcessTask.setTaskName(end.getTaskName());
            actProcessTask.setProcessfName(actProcdef.getActName());
            actProcessTask.setProcessId(processId);
            actProcessTask.setContent("流程审核结束");
            actProcessTask.setTaskNo(end.getTaskNo());
            actProcessTask.setSort(end.getSort());
            actProcessTask.setAuditUser(task.getAuditUser());
            actProcessTask.setStartTime(LocalDateTime.now());
            actProcessTask.setEndTime(LocalDateTime.now());
            actProcessTask.setTaskfId(end.getId());
            actProcessTask.setPreTaskId(task.getId());
            actProcessTask.setProcessfId(actProcdef.getId());
            String taskId = super.insert(actProcessTask);
            //审核后回调
            Map<String, Object> map = afterParamDispose(taskNode.getId(), approveReqDto.getOtherParam());
            afterNotify(processId, actProcess.getClassName(), approveReqDto.getOperation(), map);
            ActProcess endProcess = new ActProcess();
            endProcess.setId(processId);
            endProcess.setState(ProcessStateEnum.END.getName());
            endProcess.setEndTime(LocalDateTime.now());
            super.updateById(endProcess);
            taskRecord(taskId, end.getId(), parentHistory);
            processTaskRecord(taskId, processId);
            //流程结束回调
            endNotify(processId, actProcess.getClassName(), approveReqDto.getOperation());
            return ResultDTO.success("成功");
        }

        actTaskProcdefs.forEach(it -> {
            ActProcessTask actProcessTask = new ActProcessTask();
            actProcessTask.setState(ProcessTaskStateEnum.RUN.getName());
            actProcessTask.setTaskName(it.getTaskName());
            actProcessTask.setProcessId(processId);
            actProcessTask.setTaskNo(it.getTaskNo());
            actProcessTask.setSort(it.getSort());
            actProcessTask.setProcessfName(actProcdef.getActName());
            actProcessTask.setStartTime(LocalDateTime.now());
            actProcessTask.setTaskfId(it.getId());
            actProcessTask.setTagIds(it.getTagIds());
            actProcessTask.setPreTaskId(task.getId());
            String preTaskIds = it.getPreTaskIds();
            StringBuilder pStr = new StringBuilder();
            if(preTaskIds.contains(",") && !StringUtils.equals(it.getCond(), "or")){
                String sql = actProcessTask.getQuerySql() + " where is_deleted = 0 and voucher = 'true' and taskf_id in " + super.conditionByIn(preTaskIds, String.class);
                List<ActProcessTask> list = super.list(sql, ActProcessTask.class);
                String ids = list.stream().map(i -> {
                    pStr.append(i.getSelfHistory().substring(i.getSelfHistory().lastIndexOf(",")+1)+"|");
                    return i.getId();
                }).collect(Collectors.joining(","));
                actProcessTask.setPreTaskId(ids);
            }

            String pStrRes = pStr.toString();
            if(StringUtils.isNotEmpty(pStrRes)){
                pStrRes = ","+pStrRes.toString().substring(0, pStr.length() - 1);
            }

            actProcessTask.setProcessfId(actProcdef.getId());
            actProcessTask.setVoucher("true");
            actProcessTask.setVoucherCount(it.getNextTaskIds().split(",").length);
            String taskId = super.insert(actProcessTask);
            taskRecord(taskId, it.getId(), parentHistory + pStrRes);
            processTaskRecord(taskId, processId);
            //扣减凭证次数
            for(String id :actProcessTask.getPreTaskId().split(",")){
                deductionVoucher(id);
            }
        });
        Map<String, Object> map = afterParamDispose(taskNode.getId(), approveReqDto.getOtherParam());
        //审核后回调
        afterNotify(processId, actProcess.getClassName(), approveReqDto.getOperation(), map);
        return ResultDTO.success("成功");
    }

    /**
     * 扣减凭证次数
     * @param id
     */
    private void deductionVoucher(String id) {
        ActProcessTask actProcessTask = super.getById(id, ActProcessTask.class);
        Integer voucherCount = actProcessTask.getVoucherCount();
        String voucher = actProcessTask.getVoucher();
        if(StringUtils.equals("false", voucher)){
            throw new RuntimeException("审批流程有误，本次审核失败");
        }
        voucherCount --;
        if(voucherCount < 1){
            voucher = "false";
        }
        actProcessTask = new ActProcessTask();
        actProcessTask.setVoucher(voucher);
        actProcessTask.setVoucherCount(voucherCount);
        actProcessTask.setId(id);
        super.updateById(actProcessTask);
    }


    private Map<String, Object>   afterParamDispose(String taskId, String businessParams){
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("currentNode", super.getById(taskId, ActProcessTask.class));
        map.put("businessParams", Objects.isNull(businessParams) ? "": businessParams);
        return map;
    }

    /**
     * 审核拒绝
     * @param reqDto
     * @return
     */

    @Override
    @FLowLock
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO refuse(RefuseReqDto reqDto) {
        String processId = reqDto.getProcessId();
        ActProcess actProcess = super.getById(processId, ActProcess.class);
        if(Objects.isNull(actProcess)){
            return ResultDTO.failed("失败");
        }
        if(StringUtils.equals(actProcess.getState(), ProcessStateEnum.END.getName())){
            return  ResultDTO.failed("流程已经结束,无法审核");
        }
        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
        ProcessCommonModel processCommonModel = new ProcessCommonModel(processId, reqDto.getOperation(), true);
        boolean audit = processCommonModel.isAudit();
        if(!audit){
            return ResultDTO.failed("没有审核权限");
        }
        boolean b = beforeNotify(processId, actProcess.getClassName(), reqDto.getOperation());
        if(!b){
            return ResultDTO.failed("业务条件校验不通过，审核终止");
        }
        TaskNode taskNode = processCommonModel.getTaskNode();
        if(StringUtils.equals(taskNode.getTaskNo(), "start")){
            return ResultDTO.failed("start节点不能回退");
        }
        ActProcessTask task = new ActProcessTask();
        task.setId(taskNode.getId());
        task.setContent(reqDto.getContent());
        task.setEndTime(LocalDateTime.now());
        task.setState(ProcessTaskStateEnum.BACK.getName());
        task.setVoucher("false");

        User currentUser = getCurrentUser();
        if(Objects.nonNull(currentUser)){
            task.setAuditUser(currentUser.getUserName());
        }
        Optional.ofNullable(processCommonModel.getT()).ifPresent(it ->
                task.setExpansionData(new LinkedHashMap(){{
                    put("key1", it);
                }})
        );
        super.updateById(task);
        ActTaskProcdef actTaskProcdef = super.getById(taskNode.getTaskTemplateId(), ActTaskProcdef.class);
        ActProcessTask backNode = super.getById(taskNode.getId(), ActProcessTask.class);
        if(StringUtils.equals(backNode.getTaskNo(), "start")){
            return ResultDTO.failed("流程开始节点不允许回退");
        }
        //计算当前节点波及节点
        String parents = actTaskProcdef.getPreTaskIds();
        Set<String> scopeIdsSet = new HashSet<>();
        if(StringUtils.isNotEmpty(parents)){
            Set<String> childIds = new HashSet<>();
            List<String> list = Arrays.asList(parents.split(","));
            list.forEach(it -> {
                ActTaskProcdef actTF = super.getById(it, ActTaskProcdef.class);
                for(String childId : actTF.getNextTaskIds().split(",")){
                    childIds.add(childId);
                }
            });
            childIds.forEach(it -> {
                ActTaskProcdef  actTF = super.getById(it, ActTaskProcdef.class);
                for (String parentId : actTF.getPreTaskIds().split(",")){
                    scopeIdsSet.add(parentId);
                }
            });
            scopeIdsSet.forEach(it -> {
                String sql = " update "+ backNode.getTableName() + " set voucher = 'false' , state = '"+ProcessTaskStateEnum.INTERRUPTED.getName()+"', \"content\" = 'task:"+task.getId()
                        +"任务节点执行撤回，相关连的任务中断'" +" where state = 'RUN' and is_deleted = 0 and taskf_id != '"+actTaskProcdef.getId()+"' and self_history like '%"+it+"%'";
                jdbcTemplate.update(sql);
            });

        }
        String scopeIds = scopeIdsSet.stream().collect(Collectors.joining(","));
        //校验下一层节点
        String backFTasks = actTaskProcdef.getBackTasks();
        //审核回退节点
        ActProcessTask currentNode = super.getById(task.getId(), ActProcessTask.class);
        String selfHistory = currentNode.getSelfHistory();
        if(StringUtils.isNotEmpty(backFTasks)){
            List<ActTaskProcdef> actTaskProcdefs = super.selectByIds(backFTasks, ActTaskProcdef.class);
            if(CollectionUtils.isEmpty(actTaskProcdefs)){
                return ResultDTO.failed("节点模板："+actTaskProcdef.getId()+" 未知退回节点");
            }

            //获取最近的back任务
            List<String> taskIds = Arrays.asList((scopeIds+"," +backFTasks).split(",")).stream().map(it -> {
                if(selfHistory.lastIndexOf(it) == -1){
                    return null;
                }
                try {
                    String t = selfHistory.substring(0, selfHistory.lastIndexOf(it) - 1);
                    String taskId = t.substring(t.length() - 19);
                    return taskId;
                }catch (Exception e){
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());

            interrupted(taskIds, "task:"+ currentNode.getId() +" 审核拒绝,触发中断操作", "" );

            actTaskProcdefs.forEach(it -> {
                ActProcessTask actProcessTask = new ActProcessTask();
                actProcessTask.setState(ProcessTaskStateEnum.RUN.getName());
                actProcessTask.setTaskName(it.getTaskName());
                actProcessTask.setProcessId(processId);
                actProcessTask.setTaskNo(it.getTaskNo());
                actProcessTask.setSort(it.getSort());
                actProcessTask.setProcessfName(actProcdef.getActName());
                actProcessTask.setStartTime(LocalDateTime.now());
                actProcessTask.setTaskfId(it.getId());
                actProcessTask.setTagIds(it.getTagIds());
                actProcessTask.setPreTaskId(task.getId());
                actProcessTask.setProcessfId(actProcdef.getId());
                actProcessTask.setVoucher("true");
                actProcessTask.setVoucherCount(it.getNextTaskIds().split(",").length);
                actProcessTask.setExpansionData(new HashMap<String, Object>(){{
                    put("message", "任务实例："+task.getId() +" 审核拒绝");
                }});
                String taskId = super.insert(actProcessTask);

                taskRecord(taskId, it.getId(),  task.getSelfHistory());
                processTaskRecord(taskId, processId);
            });
            //审核后回调
            Map<String, Object> map = afterParamDispose(taskNode.getId(), reqDto.getOtherParam());
            afterNotify(processId, actProcess.getClassName(), reqDto.getOperation(), map);
            return ResultDTO.success("成功");
        }
        //如果没有回退节点
        if(StringUtils.isEmpty(actTaskProcdef.getPreTaskIds())){
            return ResultDTO.failed("节点模板："+actTaskProcdef.getId()+" 没有上一级节点");
        }

        List<ActTaskProcdef> actTaskProcdefs = super.selectByIds(actTaskProcdef.getPreTaskIds(), ActTaskProcdef.class);

        List<String> taskIds = Arrays.asList(scopeIds.split(",")).stream().map(it -> {
            if(selfHistory.lastIndexOf(it) == -1){
                return null;
            }
            try {
                String t = selfHistory.substring(0, selfHistory.lastIndexOf(it) - 1);
                String taskId = t.substring(t.length() - 19);
                return taskId;
            }catch (Exception e){
                return null;
            }

        }).filter(Objects::nonNull).collect(Collectors.toList());

        interrupted(taskIds, "task:"+ currentNode.getId() +" 审核拒绝,触发中断操作", "" );

        actTaskProcdefs.forEach(it -> {
            ActProcessTask actProcessTask = new ActProcessTask();
            actProcessTask.setState(ProcessTaskStateEnum.RUN.getName());
            actProcessTask.setTaskName(it.getTaskName());
            actProcessTask.setProcessId(processId);
            actProcessTask.setTaskNo(it.getTaskNo());
            actProcessTask.setSort(it.getSort());
            actProcessTask.setProcessfName(actProcdef.getActName());
            actProcessTask.setStartTime(LocalDateTime.now());
            actProcessTask.setTaskfId(it.getId());
            actProcessTask.setTagIds(it.getTagIds());
            actProcessTask.setPreTaskId(task.getId());
            actProcessTask.setProcessfId(actProcdef.getId());
            actProcessTask.setVoucher("true");
            actProcessTask.setVoucherCount(it.getNextTaskIds().split(",").length);
            actProcessTask.setExpansionData(new HashMap<String, Object>(){{
                put("message", "任务实例："+task.getId() +" 审核拒绝");
            }});
            String taskId = super.insert(actProcessTask);
            taskRecord(taskId, it.getId(),  backNode.getSelfHistory());
            processTaskRecord(taskId, processId);
        });
        Map<String, Object> map = afterParamDispose(taskNode.getId(), reqDto.getOtherParam());
        afterNotify(processId, actProcess.getClassName(), reqDto.getOperation(), map);
        return ResultDTO.success("成功");
    }

    /**
     * 撤回
     * 规则如下：
     * 1.当前父节点有多个不予撤回
     * 2.当前如果是审核拒绝回退的不予撤回
     * @param reqDto
     * @return
     */
    @Override
    @FLowLock
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO withdraw(WithdrawReqDto reqDto) {
        String processId = reqDto.getProcessId();
        ActProcess actProcess = super.getById(processId, ActProcess.class);
        if(Objects.isNull(actProcess)){
            return ResultDTO.failed("失败");
        }
        if(StringUtils.equals(actProcess.getState(), ProcessStateEnum.END.getName())){
            return  ResultDTO.failed("流程已经结束,无法撤回");
        }
        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
        ProcessCommonModel processCommonModel = new ProcessCommonModel(processId, reqDto.getOperation(), true);
        boolean withdraw = processCommonModel.isWithdraw();
        if(!withdraw){
            return  ResultDTO.failed("撤回条件不满足, 不予受理");
        }
        boolean b = beforeNotify(processId, actProcess.getClassName(), reqDto.getOperation());
        if(!b){
            return ResultDTO.failed("业务条件校验不通过，撤回终止");
        }
        TaskNode taskNode = processCommonModel.getTaskNode();
        ActProcessTask task = new ActProcessTask();
        task.setId(taskNode.getId());
        task.setContent(String.valueOf(reqDto.getContent()) +"; 当前任务执行撤回中断");
        task.setEndTime(LocalDateTime.now());
        task.setState(ProcessTaskStateEnum.INTERRUPTED.getName());
        User currentUser = getCurrentUser();
        if(Objects.nonNull(currentUser)){
            task.setAuditUser(currentUser.getUserName());
        }
        Optional.ofNullable(processCommonModel.getT()).ifPresent(it ->
                task.setExpansionData(new LinkedHashMap(){{
                    put("key1", it);
                }})
        );
        super.updateById(task);
        ActProcessTask processTask = super.getById(task.getId(), ActProcessTask.class);
        String preTaskId = processTask.getPreTaskId();
        if(StringUtils.isEmpty(preTaskId)){
            return ResultDTO.failed("当前任务没有前一个任务节点, 操作失败");
        }
        //计算波动范围
        ActTaskProcdef actTaskProcdef = super.getById(processTask.getTaskfId(), ActTaskProcdef.class);
        String parents = actTaskProcdef.getPreTaskIds();
        Set<String> scopeIdsSet = new HashSet<>();
        if(StringUtils.isNotEmpty(parents)){
            Set<String> childIds = new HashSet<>();
            List<String> list = Arrays.asList(parents.split(","));
            list.forEach(it -> {
                ActTaskProcdef actTF = super.getById(it, ActTaskProcdef.class);
                for(String childId : actTF.getNextTaskIds().split(",")){
                    childIds.add(childId);
                }
            });
            childIds.forEach(it -> {
                ActTaskProcdef  actTF = super.getById(it, ActTaskProcdef.class);
                for (String parentId : actTF.getPreTaskIds().split(",")){
                    scopeIdsSet.add(parentId);
                }
            });
            ActProcessTask finalProcessTask = processTask;
            scopeIdsSet.forEach(it -> {
                String sql = " update "+ finalProcessTask.getTableName() + " set voucher = 'false' , state = '"+ProcessTaskStateEnum.INTERRUPTED.getName()+"', \"content\" = 'task:"+task.getId()
                        +"任务节点执行撤回，相关连的任务中断'" +" where state = 'RUN' and is_deleted = 0 and taskf_id != '"+actTaskProcdef.getId()+"' and self_history like '%"+it+"%'";
                jdbcTemplate.update(sql);
            });


        }
        String scopeIds = scopeIdsSet.stream().collect(Collectors.joining(","));
        String scopeTaskIds = scopeIdsSet.stream().collect(Collectors.joining(","));

        interrupted(Arrays.asList(scopeIds), "task:" + task.getId() + " 任务节点执行撤回，相关连的任务中断", "");
        processTask = super.getById(preTaskId, ActProcessTask.class);
        ActProcessTask actProcessTask = new ActProcessTask();
        actProcessTask.setState(ProcessTaskStateEnum.RUN.getName());
        actProcessTask.setTaskName(processTask.getTaskName());
        actProcessTask.setProcessId(processId);
        actProcessTask.setTaskNo(processTask.getTaskNo());
        actProcessTask.setSort(processTask.getSort());
        actProcessTask.setProcessfName(processTask.getProcessfName());
        actProcessTask.setStartTime(LocalDateTime.now());
        actProcessTask.setTaskfId(processTask.getTaskfId());
        actProcessTask.setTagIds(processTask.getTagIds());
        actProcessTask.setPreTaskId(task.getId());
        actProcessTask.setProcessfId(actProcessTask.getProcessfId());
        actProcessTask.setVoucher("true");
        actProcessTask.setVoucherCount(1);
        super.insert(actProcessTask);
        Map<String, Object> map = afterParamDispose(taskNode.getId(), reqDto.getOtherParam());
        afterNotify(processId, actProcess.getClassName(), reqDto.getOperation(), map);
        return ResultDTO.success("成功");
    }

    @Override
    public ResultDTO getDetail(String processId) {
        //ProcessCommonModel processCommonModel = new ProcessCommonModel(processId);
        ActProcess actProcess = super.getById(processId, ActProcess.class);
        ProcessRespDto processRespDto = (ProcessRespDto)actProcess.copyProperties(ProcessRespDto.class);
        Optional.ofNullable(actProcess.getState()).ifPresent(it -> processRespDto.setState(ProcessStateEnum.getByName(it).getDescription()));
        return ResultDTO.success(actProcess);
    }


    /**
     * 获取任务历史
     * @param processId
     * @return
     */
    @Override
    public ResultDTO getTaskHistory(String processId) {
        ActProcess actProcess = super.getById(processId, ActProcess.class);
        String taskHistory = actProcess.getTaskHistory();
        if(StringUtils.isEmpty(taskHistory)){
            return ResultDTO.success(null);
        }
        String[] taskIds = taskHistory.split(",");
        String joinSql = "";
        for(int i = 0; i < taskIds.length; i++){
            joinSql += " UNION all select '" + taskIds[i] +"' id, "+ i +" sort";
        }
        joinSql = joinSql.replaceFirst("UNION all", "");
        String sql = "select a.* from " +new ActProcessTask().getTableName() + " a, (" + joinSql + ") b where a.id = b.id order by b.sort";
        List<ActProcessTask> list = super.list(sql, ActProcessTask.class);
        List<ActProcessTaskRespDto> respDtos = list.stream().map(it -> {
            ActProcessTaskRespDto actProcessTaskRespDto = (ActProcessTaskRespDto) it.copyProperties(ActProcessTaskRespDto.class);
            actProcessTaskRespDto.setState(ProcessTaskStateEnum.getByName(it.getState()).getDescription());
            return actProcessTaskRespDto;
        }).collect(Collectors.toList());
        return ResultDTO.success(respDtos);
    }


    private boolean beforeNotify(String processId, String hookName, ProcessOperationEnum processOperationEnum){
        if(StringUtils.isEmpty(hookName)){
            return true;
        }
        if(annotationOpenUtils.isOpen()){
            //基于注解通知
            Object process = null;
            try {
                process = FlowClassLoader.flowClassLoader(Class.forName(hookName));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }finally {
                if(process == null){
                    return true;
                }
            }
            Method method = MethodLoader.getMethod(process.getClass().getMethods(), FlowAuditBefore.class);
            if(Objects.isNull(method)){
                method = MethodLoader.getMethod(process.getClass().getDeclaredMethods(), FlowAuditBefore.class);
            }
            if(Objects.isNull(method)){
                return true;
            }
            FlowAuditBefore annotation = method.getAnnotation(FlowAuditBefore.class);
            String[] processTemplateIds = annotation.processTemplateIds();
            boolean f = false;
            if(processTemplateIds.length == 0){
                f = true;
            }
            ActProcess actProcess = super.getById(processId, ActProcess.class);
            ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
            String actNo = actProcdef.getActNo();

            for(String processTemplate : processTemplateIds){
                if(f){
                    break;
                }
                f = StringUtils.equals(actNo, processTemplate);
            }

            if(!f){
                return true;
            }
            BusParam.getInstance().setMap(new LinkedHashMap(){{
                put(ParamKey.CONTENTKEY, new ProcessCommonModel(processId, processOperationEnum, true));
            }});

            try {
                f = (boolean) method.invoke(process);
                return f;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }finally {
                return true;
            }
        }else {

            try {
                Class<?> aClass = Class.forName(hookName);
                Method auditBefore = aClass.getMethod("auditBefore");
                if(Objects.isNull(auditBefore)){
                    return true;
                }

                IProcess hook = (IProcess)FlowBeanFactory.getInstance().getBean(aClass);
                return hook.auditBefore(new ProcessCommonModel(processId, processOperationEnum, true));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                return true;
            }
        }
    }



    private void afterNotify(String processId, String hookName, ProcessOperationEnum processOperationEnum, Map<String, Object> businessMap){
        if(StringUtils.isEmpty(hookName)){
            return;
        }
        ActProcess actProcess = super.getById(processId, ActProcess.class);
        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
        String actNo = actProcdef.getActNo();
        if(annotationOpenUtils.isOpen()){

            //基于注解通知
            Object process = null;
            try {
                process = FlowClassLoader.flowClassLoader(Class.forName(hookName));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(Objects.isNull(process)){
                return;
            }
            Method method = MethodLoader.getMethod(process.getClass().getMethods(), FlowAuditAfter.class);
            if(Objects.isNull(method)){
                method = MethodLoader.getMethod(process.getClass().getDeclaredMethods(), FlowAuditAfter.class);
            }
            if(Objects.isNull(method)){
                return;
            }
            FlowAuditAfter annotation = method.getAnnotation(FlowAuditAfter.class);
            String[] processTemplateIds = annotation.processTemplateIds();
            boolean f = false;
            if(processTemplateIds.length == 0){
                f = true;
            }


            for(String processTemplate : processTemplateIds){
                if(f){
                    break;
                }
                f = StringUtils.equals(actNo, processTemplate);
            }
            //未匹配到结果，不通知
            if(!f){
                return;
            }
            FlowNotify flowNotify = new FlowNotify();
            flowNotify.setNotifyEnum(NotifyEnum.TASKAUDITAFTER);
            flowNotify.setHook(process);
            flowNotify.setProcessId(processId);
            flowNotify.setProcessOperationEnum(processOperationEnum);
            flowNotify.getBusiness().putAll(businessMap);
            flowNotify.setActNo(actNo);
            //是否异步通知
            boolean annotationPresent = method.isAnnotationPresent(FlowAsSync.class);
            if(annotationPresent){
                publisher.publishEvent(new FlowRegisterEvent(flowNotify));
            }else {
                ProcessCommonModel processCommonModel = new ProcessCommonModel();
                processCommonModel.setProcessId(flowNotify.getProcessId());
                processCommonModel.setOperation(flowNotify.getProcessOperationEnum());
                processCommonModel.setAtcNo(actNo);
                Map<String, Object> business = flowNotify.getBusiness();
                processCommonModel.afterInit((ActProcessTask)business.get("currentNode"), String.valueOf(business.get("businessParams")));
                BusParam.getInstance().setMap(new LinkedHashMap(){{
                    put(ParamKey.CONTENTKEY, processCommonModel);
                    put(ParamKey.ACTNO, actNo);
                }});
                try {
                    method.invoke(process);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

            }


        }else {
            try {
                Class<?> aClass = Class.forName(hookName);
                IProcess hook = (IProcess)FlowBeanFactory.getInstance().getBean(aClass);

                FlowNotify flowNotify = new FlowNotify();
                flowNotify.setNotifyEnum(NotifyEnum.TASKAUDITAFTER);
                flowNotify.setHook(hook);
                flowNotify.setProcessId(processId);
                flowNotify.setProcessOperationEnum(processOperationEnum);
                flowNotify.getBusiness().putAll(businessMap);
                flowNotify.setActNo(actNo);

                //是否异步回调
                Method auditAfter = hook.getClass().getMethod("auditAfter", IProcess.class);
                boolean annotationPresent = auditAfter.isAnnotationPresent(FlowAsSync.class);
                //异步调用
                if(annotationPresent){
                    publisher.publishEvent(new FlowRegisterEvent(flowNotify));
                }
                //同步调用
                else {
                    ProcessCommonModel processCommonModel = new ProcessCommonModel();
                    processCommonModel.setProcessId(flowNotify.getProcessId());
                    processCommonModel.setOperation(flowNotify.getProcessOperationEnum());
                    processCommonModel.setAtcNo(actNo);
                    Map<String, Object> business = flowNotify.getBusiness();
                    processCommonModel.afterInit((ActProcessTask)business.get("currentNode"), String.valueOf(business.get("businessParams")));
                    flowNotify.getHook().auditAfter(processCommonModel);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }






    }



    private void endNotify(String processId, String hookName, ProcessOperationEnum processOperationEnum){
        if(StringUtils.isEmpty(hookName)){
            return;
        }

        ActProcess actProcess = super.getById(processId, ActProcess.class);
        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
        String actNo = actProcdef.getActNo();

        if(annotationOpenUtils.isOpen()){
            //基于注解通知
            Object process = null;
            try {
                process = FlowClassLoader.flowClassLoader(Class.forName(hookName));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(Objects.isNull(process)){
                return;
            }
            Method method = MethodLoader.getMethod(process.getClass().getMethods(), FlowAuditEnd.class);
            if(Objects.isNull(method)){
                method = MethodLoader.getMethod(process.getClass().getDeclaredMethods(), FlowAuditEnd.class);
            }
            if(Objects.isNull(method)){
                return;
            }
            FlowAuditEnd annotation = method.getAnnotation(FlowAuditEnd.class);
            String[] processTemplateIds = annotation.processTemplateIds();
            boolean f = false;
            if(processTemplateIds.length == 0){
                f = true;
            }


            for(String processTemplate : processTemplateIds){
                if(f){
                    break;
                }
                f = StringUtils.equals(actNo, processTemplate);
            }
            //未匹配到结果，不通知
            if(!f){
                return;
            }
            FlowNotify flowNotify = new FlowNotify();
            flowNotify.setNotifyEnum(NotifyEnum.END);
            flowNotify.setHook(process);
            flowNotify.setProcessId(processId);
            flowNotify.setActNo(actNo);
            //是否异步通知
            boolean annotationPresent = method.isAnnotationPresent(FlowAsSync.class);
            if(annotationPresent){
                publisher.publishEvent(new FlowRegisterEvent(flowNotify));
            }else {
                ProcessCommonModel processCommonModel = new ProcessCommonModel();
                processCommonModel.setProcessId(flowNotify.getProcessId());
                processCommonModel.setOperation(flowNotify.getProcessOperationEnum());
                processCommonModel.setAtcNo(actNo);
                BusParam.getInstance().setMap(new LinkedHashMap(){{
                    put(ParamKey.CONTENTKEY, processCommonModel);
                    put(ParamKey.ACTNO, actNo);
                }});
                try {
                    method.invoke(process);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }





        }else {

            try {
                Class<?> aClass = Class.forName(hookName);
                IProcess hook = (IProcess)FlowBeanFactory.getInstance().getBean(aClass);
                FlowNotify flowNotify = new FlowNotify();
                flowNotify.setNotifyEnum(NotifyEnum.END);
                flowNotify.setHook(hook);
                flowNotify.setProcessId(processId);
                flowNotify.setActNo(actNo);
                //是否异步回调
                Method auditAfter = hook.getClass().getMethod("auditAfter", IProcess.class);
                boolean annotationPresent = auditAfter.isAnnotationPresent(FlowAsSync.class);
                if(annotationPresent){
                    publisher.publishEvent(new FlowRegisterEvent(flowNotify));
                }else {
                    ProcessCommonModel processCommonModel = new ProcessCommonModel();
                    processCommonModel.setProcessId(flowNotify.getProcessId());
                    processCommonModel.setOperation(flowNotify.getProcessOperationEnum());
                    processCommonModel.setAtcNo(actNo);
                    flowNotify.getHook().goEndBack(processCommonModel);
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }







    }


    private void startNotify(FlowNotify flowNotify, IProcess hook){
        if(Objects.isNull(hook)){
            return;
        }
        ActProcess actProcess = super.getById(flowNotify.getProcessId(), ActProcess.class);
        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
        String actNo = actProcdef.getActNo();
        flowNotify.setActNo(actNo);
        try {
            //是否异步回调
            Method auditAfter = hook.getClass().getMethod("start", IProcess.class);
            boolean annotationPresent = auditAfter.isAnnotationPresent(FlowAsSync.class);
            if(annotationPresent){
                publisher.publishEvent(new FlowRegisterEvent(flowNotify));
            }else {
                ProcessCommonModel processCommonModel = new ProcessCommonModel();
                processCommonModel.setProcessId(flowNotify.getProcessId());
                processCommonModel.setAtcNo(actNo);
                flowNotify.getHook().start(processCommonModel);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void startNotify(FlowNotify flowNotify){
        Object hook = flowNotify.getHook(annotationOpenUtils.isOpen());
        if(Objects.isNull(hook)){
            return;
        }
        ActProcess actProcess = super.getById(flowNotify.getProcessId(), ActProcess.class);
        ActProcdef actProcdef = super.getById(actProcess.getActId(), ActProcdef.class);
        String actNo = actProcdef.getActNo();
        flowNotify.setActNo(actNo);
        Method method = MethodLoader.getMethod(hook.getClass().getMethods(), FlowAuditStart.class);
        if(Objects.isNull(method)){
            method = MethodLoader.getMethod(hook.getClass().getDeclaredMethods(), FlowAuditStart.class);
        }

        if(Objects.isNull(hook) || Objects.isNull(method)){
            return;
        }
        FlowAuditStart annotation = method.getAnnotation(FlowAuditStart.class);
        String[] processTemplateIds = annotation.processTemplateIds();
        boolean f = false;
        if(processTemplateIds.length == 0){
            f = true;
        }


        for(String processTemplate : processTemplateIds){
            if(f){
                break;
            }
            f = StringUtils.equals(actNo, processTemplate);
        }
        //未匹配到结果，不通知
        if(!f){
            return;
        }

        try {
            //是否异步回调
            boolean annotationPresent = method.isAnnotationPresent(FlowAsSync.class);
            if(annotationPresent){
                publisher.publishEvent(new FlowRegisterEvent(flowNotify));
            }else {
                ProcessCommonModel processCommonModel = new ProcessCommonModel();
                processCommonModel.setProcessId(flowNotify.getProcessId());
                processCommonModel.setAtcNo(actNo);
                BusParam.getInstance().setMap(new LinkedHashMap(){{
                    put(ParamKey.CONTENTKEY, processCommonModel);
                    put(ParamKey.ACTNO, actNo);
                }});
                method.invoke(hook);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






    public List<TaskNode> getCurrentTask(String processId) {
        ActProcessTask actProcessTask = new ActProcessTask();
        String sql = actProcessTask.getQuerySql()+" where is_deleted = 0  and process_id = ? and state = ? order by sort, create_time  ";
        List<ActProcessTask> list = super.list(sql, ActProcessTask.class, new Object[]{processId, ProcessTaskStateEnum.RUN.getName()});
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<TaskNode> collect = list.stream().map(it -> {
            TaskNode taskNode = new TaskNode();
            taskNode.setTaskName(it.getTaskName());
            taskNode.setTaskNo(it.getTaskNo());
            taskNode.setId(it.getId());
            taskNode.setTaskTemplateId(it.getTaskfId());
            taskNode.setState(ProcessTaskStateEnum.getByName(it.getState()));
            return taskNode;
        }).collect(Collectors.toList());

        return collect;
    }

    public ActProcessTask getTaskById(String id) {
        return super.getById(id, ActProcessTask.class);
    }

    public List<ActProcessTask> getPreTaskById(String id) {
        ActProcessTask task = getTaskById(id);
        List<ActProcessTask> list = super.selectByIds(task.getPreTaskId(), ActProcessTask.class);
        return list;
    }



    private boolean conditionLoad(String condition, Map<String, Boolean> map,String message, String voucher){
        if(StringUtils.isEmpty(condition)){
            condition = "and";
        }
        if(StringUtils.equals("and", condition)){
            Optional<Boolean> first = map.values().stream().filter(it -> !it).findFirst();
            return first.isPresent() ? first.get() : true;
        }
        List<String> ids = new ArrayList<>();
        map.forEach((k, v) -> {if(!v) ids.add(k);});
        String idsStr = ids.stream().collect(Collectors.joining(","));

        List<ActTaskProcdef> actTaskProcdefs = super.selectByIds(idsStr, ActTaskProcdef.class);

        String sql1 = "update " + new ActProcessTask().getTableName() + " set \"content\" = ? , \"state\" = ?, \"voucher\" = 'false'   where is_deleted = 0 " +
                " and state = '"+ProcessTaskStateEnum.RUN.getName()+"' and voucher = 'true'" +
                " and taskf_id in " + super.conditionByIn(idsStr, String.class);
        jdbcTemplate.update(sql1, message, ProcessTaskStateEnum.INTERRUPTED.getName());

//        Set<String> collect = actTaskProcdefs.stream().filter(it -> !StringUtils.isEmpty(it.getTagIds())).map(it -> it.getTagIds()).collect(Collectors.toSet());
//        if(CollectionUtils.isEmpty(collect)){
//            return true;
//        }
//        Set<String> ids2 = new HashSet<>();
//        collect.forEach(it -> Arrays.asList(it.split(",")).forEach(ids2::add) );
//        interrupted(ids2, message, voucher);
        return true;
    }

    /**
     * 中断标签运行中的任务
     * @param ids
     * @param message
     * @param voucher
     */
    private void interrupted(Collection<String> ids, String message, String voucher){
        ids.forEach(it -> {
            String sql = "update " +new ActProcessTask().getTableName() + " set \"content\" = ? , \"state\" = ?, \"voucher\" = 'false'  where is_deleted = 0 " +
                    " and voucher = 'true' and  state = '"+ProcessTaskStateEnum.RUN.getName()+"' and self_history like '%"+it+"%' ";
            jdbcTemplate.update(sql, message, ProcessTaskStateEnum.INTERRUPTED.getName());
        });
    }


    //taskNode 节点记录
    private void taskRecord(String taskId, String taskfId, String parentHistory){
        ActProcessTask actProcessTask = super.getById(taskId, ActProcessTask.class);
        String res = "";
        if(StringUtils.isEmpty(parentHistory)){
            res = actProcessTask.getId() + "-" + taskfId;
        }else {
            res = parentHistory + ","+ actProcessTask.getId() + "-" + taskfId;
        }
        actProcessTask = new ActProcessTask();
        actProcessTask.setId(taskId);
        actProcessTask.setSelfHistory(res);
        super.updateById(actProcessTask);
    }


    /**
     * 流程任务流程记录
     * @param taskId
     * @param processId
     */
    private void processTaskRecord(String taskId, String processId){
        ActProcess process = super.getById(processId, ActProcess.class);
        String res = "";
        if(StringUtils.isEmpty(process.getTaskHistory())){
            res =  taskId;
        }else {
            res += ","+ taskId;
        }
        process = new ActProcess();
        process.setId(processId);
        process.setTaskHistory(res);
        super.updateById(process);
    }










}
