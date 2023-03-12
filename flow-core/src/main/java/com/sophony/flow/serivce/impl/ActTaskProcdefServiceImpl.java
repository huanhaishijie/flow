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
 * @version 1.0
 * @description
 * @date 2023/3/9 20:17
 */
@Service
public class ActTaskProcdefServiceImpl extends BaseService implements IActTaskProcdefService {


    @Override
    public ResultDTO save(ActTaskProcdefReqDto reqDto) {
        ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
        actTaskProcdef.copyProperties(reqDto);
        if(!CollectionUtils.isEmpty(reqDto.getNextTaskIds())){
            String ids = reqDto.getNextTaskIds().stream().collect(Collectors.joining(","));
            actTaskProcdef.setNextTaskIds(ids);
        }
        if(!CollectionUtils.isEmpty(reqDto.getPreTaskIds())){
            String ids = reqDto.getPreTaskIds().stream().collect(Collectors.joining(","));
            actTaskProcdef.setPreTaskIds(ids);
        }

        if(!StringUtils.isNotBlank(reqDto.getProcessFid())){
            throw new RuntimeException("没有审批流程模板id");
        }

        if(!CollectionUtils.isEmpty(reqDto.getNextTaskIds()) && !CollectionUtils.isEmpty(reqDto.getPreTaskIds())){
            boolean b = reqDto.getNextTaskIds().retainAll(reqDto.getPreTaskIds());
            if(!b){
                throw new RuntimeException("当前节点上一级节点和下一级节点有重复");
            }
        }
        if(!CollectionUtils.isEmpty(reqDto.getTagIds())){
            String ids = reqDto.getTagIds().stream().collect(Collectors.joining(","));
            actTaskProcdef.setTagIds(ids);
        }
        if(!CollectionUtils.isEmpty(reqDto.getInterruptTag())){
            String ids = reqDto.getInterruptTag().stream().collect(Collectors.joining(","));
            actTaskProcdef.setInterruptTag(ids);
        }
        if(StringUtils.isNotBlank(actTaskProcdef.getId())){
            super.updateById(actTaskProcdef);
            return ResultDTO.success("成功");
        }
        super.insert(actTaskProcdef);
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
