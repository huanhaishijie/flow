package com.sophony.flow.serivce.impl;

import com.sophony.flow.api.reqDto.ActTaskProcdefReqDto;
import com.sophony.flow.api.respDto.ActTaskProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.commons.StringUtils;
import com.sophony.flow.mapping.ActTaskProcdef;
import com.sophony.flow.serivce.IActTaskProcdefService;
import com.sophony.flow.worker.common.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ActTaskProcdefServiceImpl
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 20:17
 */
@Service
public class ActTaskProcdefServiceImpl extends BaseService implements IActTaskProcdefService {


    @Override
    public ResultDTO save(ActTaskProcdefReqDto reqDto) {
        ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
        actTaskProcdef.copyProperties(reqDto);

        if(!CollectionUtils.isEmpty(reqDto.getPreTaskIds())){
            String ids = reqDto.getPreTaskIds().stream().collect(Collectors.joining(","));
            actTaskProcdef.setPreTaskIds(ids);
        }
        if(!StringUtils.isNotBlank(reqDto.getProcessFid())){
            return ResultDTO.failed("没有审批流程模板id");
        }
        actTaskProcdef.getQuerySql();
        ActTaskProcdef temp = super.selectOne(actTaskProcdef.getQuerySql() + " where is_deleted = 0 and task_no = ? ", ActTaskProcdef.class, new Object[]{reqDto.getTaskNo()});
        if(Objects.nonNull(temp)){
           return  ResultDTO.failed("当前节点编号已经存在，请重新命名编号");
        }

        if(StringUtils.isNotBlank(actTaskProcdef.getId())){
            ActTaskProcdef taskProcdef = super.getById(reqDto.getId(), ActTaskProcdef.class);
            if(!CollectionUtils.isEmpty(reqDto.getPreTaskIds()) && StringUtils.isNotBlank(taskProcdef.getNextTaskIds())){
                List<String> nextIds = Arrays.asList(taskProcdef.getNextTaskIds().split(","));
                boolean b = nextIds.retainAll(reqDto.getPreTaskIds());
                if(!b){
                    return ResultDTO.failed("下一级节点和上一级节点冲突");
                }
            }
            if(!CollectionUtils.isEmpty(reqDto.getPreTaskIds()) && reqDto.getPreTaskIds().contains(reqDto.getId())){
                    return ResultDTO.failed("所选上一级节点不能包含自己");
            }
            String parentSql =  actTaskProcdef.getQuerySql() + "where is_deleted = 0 and next_task_ids like '%"+actTaskProcdef.getId()+"%' ";
            try {
                List<ActTaskProcdef> parents = super.list(parentSql, ActTaskProcdef.class);
                parents.forEach(it -> {
                    ActTaskProcdef act = new ActTaskProcdef();
                    act.setId(it.getId());
                    String nextTaskIds = it.getNextTaskIds().replace(actTaskProcdef.getId()+",", "");
                    nextTaskIds = nextTaskIds.replace(actTaskProcdef.getId(), "");
                    act.setNextTaskIds(nextTaskIds);
                    super.updateById(act);
                });
            }catch (Exception e){

            }




            if(!CollectionUtils.isEmpty(reqDto.getPreTaskIds())){
                String ids = reqDto.getPreTaskIds().stream().collect(Collectors.joining(","));
                List<ActTaskProcdef> actTaskProcdefs = super.selectByIds(ids, ActTaskProcdef.class);
                actTaskProcdefs.forEach(it -> {
                    ActTaskProcdef actf = new ActTaskProcdef();
                    if(StringUtils.isNotBlank(it.getNextTaskIds())){
                        List<String> list = Arrays.asList(it.getNextTaskIds().split(","));
                        list.add(reqDto.getId());
                        list.sort(Comparator.reverseOrder());
                        actf.setNextTaskIds(list.stream().collect(Collectors.joining(",")));
                    }else {
                        actf.setNextTaskIds(reqDto.getId());
                    }
                    actf.setId(it.getId());
                    super.updateById(actf);
                });
            }


            super.updateById(actTaskProcdef);
            return ResultDTO.success("成功");
        }
        String id = super.insert(actTaskProcdef);
        if(!CollectionUtils.isEmpty(reqDto.getPreTaskIds())){
            String ids = reqDto.getPreTaskIds().stream().collect(Collectors.joining(","));
            List<ActTaskProcdef> actTaskProcdefs = super.selectByIds(ids, ActTaskProcdef.class);
            actTaskProcdefs.forEach(it -> {
                ActTaskProcdef actf = new ActTaskProcdef();
                if(StringUtils.isNotBlank(it.getNextTaskIds())){
                    List<String> list = Arrays.asList(it.getNextTaskIds().split(","));
                    list.add(id);
                    list.sort(Comparator.reverseOrder());
                    actf.setNextTaskIds(list.stream().collect(Collectors.joining(",")));
                }else {
                    actf.setNextTaskIds(id);
                }
                actf.setId(it.getId());
                super.updateById(actf);
            });
        }
        return ResultDTO.success("成功");
    }

    @Override
    public ResultDTO delete(String id) {
        ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
        actTaskProcdef.setId(id);
        super.deleteById(actTaskProcdef);
        return ResultDTO.success("成功");
    }

    @Override
    public ResultDTO<ActTaskProcdefRespDto> getById(String id) {
        ActTaskProcdef actTaskProcdef = super.getById(id, ActTaskProcdef.class);
        if(Objects.isNull(actTaskProcdef)){
            return null;
        }
        ActTaskProcdefRespDto actTaskProcdefRespDto = (ActTaskProcdefRespDto)actTaskProcdef.copyProperties(ActTaskProcdefRespDto.class);
        Optional.ofNullable(actTaskProcdef.getBackTasks()).ifPresent(it -> actTaskProcdefRespDto.setBackTaskDtos(getActTaskProcdefRespDtoByIds(it)));
        Optional.ofNullable(actTaskProcdef.getNextTaskIds()).ifPresent(it -> actTaskProcdefRespDto.setNextTaskIds(Arrays.asList(it.split(","))));
        Optional.ofNullable(actTaskProcdef.getTagIds()).ifPresent(it -> actTaskProcdefRespDto.setTagIds(Arrays.asList(it.split(","))));
        Optional.ofNullable(actTaskProcdef.getInterruptTag()).ifPresent(it -> actTaskProcdefRespDto.setInterruptTag(Arrays.asList(it.split(","))));
        Optional.ofNullable(actTaskProcdef.getPreTaskIds()).ifPresent(it -> actTaskProcdefRespDto.setPreTaskIds(Arrays.asList(it.split(","))));
        return ResultDTO.success(actTaskProcdefRespDto);

    }

    @Override
    public ResultDTO<List<ActTaskProcdefRespDto>> listByActId(String actId) {
        ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
        String sql = actTaskProcdef.getQuerySql() + " where is_deleted = 0 and process_fid = ? order by sort, create_time ";
        List<ActTaskProcdef> list = super.list(sql, ActTaskProcdef.class, new Object[]{actId});
        if(CollectionUtils.isEmpty(list)){
            return ResultDTO.success(new ArrayList<>());
        }
        List<ActTaskProcdefRespDto> collect = list.stream().map(it -> (ActTaskProcdefRespDto) it.copyProperties(ActTaskProcdefRespDto.class)).collect(Collectors.toList());
        return ResultDTO.success(collect);

    }


    private List<ActTaskProcdefRespDto> getActTaskProcdefRespDtoByIds(String ids){
        String sql = "select id, task_name from act_task_procdef where is_deleted on id in " + conditionByIn(ids, String.class) ;
        List<ActTaskProcdef> list = super.list(sql, ActTaskProcdef.class);
        List<ActTaskProcdefRespDto> res = list.stream().map(it -> (ActTaskProcdefRespDto) it.copyProperties(ActTaskProcdefRespDto.class)).collect(Collectors.toList());
        return res;
    }


}
