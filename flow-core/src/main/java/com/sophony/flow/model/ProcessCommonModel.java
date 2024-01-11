package com.sophony.flow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sophony.flow.commons.constant.ProcessOperationEnum;
import com.sophony.flow.commons.constant.ProcessTaskStateEnum;
import com.sophony.flow.mapping.ActProcess;
import com.sophony.flow.mapping.ActProcessTask;
import com.sophony.flow.serivce.impl.ProcessServiceImpl;
import com.sophony.flow.worker.common.FlowBeanFactory;
import com.sophony.flow.worker.modle.ProcessModel;
import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.TaskPermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ProcessModel
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 13:01
 */

@JsonIgnoreProperties({"isCleanCache", "operation", "info"})
public class ProcessCommonModel extends ProcessModel {


    public ProcessCommonModel(String processId) {
        super(processId);
    }

    public ProcessCommonModel(){

    }

    String atcNo;


    protected transient Function<ProcessCommonModel, Map<String, Object>> info = model -> new LinkedHashMap<String, Object>(){{
        put("processId", model.getProcessId());
        put("processTemplateId", model.getProcessTemplateId());
        put("operation", operation);
        put("businessParams", model.getBusinessParams());
    }};

    @Override
    public void init() {
        FlowBeanFactory flowBeanFactory = FlowBeanFactory.getInstance();
        ProcessServiceImpl service = flowBeanFactory.getBean(ProcessServiceImpl.class);
        if(Objects.isNull(service)){
            return;
        }

        ActProcess actProcess = service.getById(this.getProcessId(), ActProcess.class);
        this.setProcessTemplateId(actProcess.getActId());
        //获取当前运行的中的任务节点
        List<TaskNode> currentTask = service.getCurrentTask(getProcessId());
        if(CollectionUtils.isEmpty(currentTask)){
            this.setAudit(false);
            this.setWithdraw(false);
            return;
        }
        TaskPermission valid = flowBeanFactory.getFlowValidService().valid(currentTask, info.apply(this));
        if(CollectionUtils.isEmpty(valid.getTaskNodeList())){
            this.setAudit(false);
            this.setWithdraw(false);
            return;
        }
        this.setAudit(valid.isAudit());
        this.setT(valid.getT());
        this.setTaskNode((TaskNode) valid.getTaskNodeList().get(0));
        if(StringUtils.equals(getTaskNode().getTaskNo(), "start")){
            this.setAudit(true);
            this.setWithdraw(false);
            return;
        }
        List<ActProcessTask> preTasks = service.getPreTaskById(getTaskNode().getId());
        if(CollectionUtils.isEmpty(preTasks)){
            return;
        }
        //多节点不允许撤回
        if(preTasks.size() > 1){
            this.setWithdraw(false);
            return;
        }
        Map<String, ActProcessTask> taskMap = preTasks.stream().collect(Collectors.toMap(it -> it.getTaskNo(), it -> it));
        if(taskMap.containsKey("start")){
            this.setWithdraw(true);
            return;
        }
        List<TaskNode> list = new ArrayList();
        preTasks.forEach(task -> {
            TaskNode pre = new TaskNode();
            pre.setId(task.getId());
            pre.setTaskTemplateId(task.getTaskfId());
            pre.setTaskName(task.getTaskName());
            pre.setTaskNo(task.getTaskNo());
            list.add(pre);
        });

        valid = flowBeanFactory.getFlowValidService().valid(list, info.apply(this));
        if(CollectionUtils.isEmpty(valid.getTaskNodeList())){
            return;
        }
        this.setWithdraw(valid.isAudit());
        ActProcessTask task = service.getTaskById(((TaskNode) valid.getTaskNodeList().get(0)).getId());
        //上一级多节点不允许撤回
        if(StringUtils.isNotEmpty(task.getPreTaskId()) && task.getPreTaskId().contains(",")){
            this.setWithdraw(false);
            return;
        }
        task = service.getTaskById(task.getPreTaskId());
        //上一节点如果是审核拒绝状态不允许撤回
        if(StringUtils.equals(task.getState(), ProcessTaskStateEnum.BACK.getName()) || StringUtils.equals(task.getState(), ProcessTaskStateEnum.INTERRUPTED.getName())){
            this.setWithdraw(false);
        }

    }

    public ProcessCommonModel(String processId, ProcessOperationEnum operation, boolean isCleanCache) {
        super(processId, operation, isCleanCache);
    }

    public String getAtcNo() {
        return atcNo;
    }

    public void setAtcNo(String atcNo) {
        this.atcNo = atcNo;
    }
}
