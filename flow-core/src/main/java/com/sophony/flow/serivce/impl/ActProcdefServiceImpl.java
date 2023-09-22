package com.sophony.flow.serivce.impl;

import com.sophony.flow.api.reqDto.ActProcdefReqDto;
import com.sophony.flow.api.respDto.ActProcdefRespDto;
import com.sophony.flow.api.respDto.ActTaskProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.mapping.ActProcdef;
import com.sophony.flow.mapping.ActTaskProcdef;
import com.sophony.flow.serivce.IActProcdefService;
import com.sophony.flow.serivce.IActTaskProcdefService;
import com.sophony.flow.worker.common.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ActProcdefImpl
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程模板定义实现
 * @date 2023/3/8 19:36
 */
@Service
public class ActProcdefServiceImpl extends BaseService implements IActProcdefService {



    @Override
    @Transactional
    public ResultDTO save(ActProcdefReqDto reqDto){

        ActProcdef actProcdef = new ActProcdef();
        String sql = actProcdef.getQuerySql() + " where act_no = ? and is_deleted = 0 limit 1";
        ActProcdef res = super.selectOne(sql, ActProcdef.class , new Object[]{reqDto.getActNo()});
        if(Objects.isNull(res) && StringUtils.isNotEmpty(reqDto.getId())){
            res = super.getById(reqDto.getId(), ActProcdef.class);
        }
        BeanUtils.copyProperties(reqDto, actProcdef);
        if(StringUtils.isEmpty(actProcdef.getId())){
            actProcdef.setState("0");
            this.insert(actProcdef);
            return ResultDTO.success("成功");
        }
        if(StringUtils.equals(res.getState(), "1")){
            return ResultDTO.failed("激活状态不能编辑");
        }
        this.updateById(actProcdef);
        return ResultDTO.success("成功");
    }



    public ResultDTO delete(String id){
        ActProcdef actProcdef = new ActProcdef();
        actProcdef.setId(id);
        super.deleteById(actProcdef);
        return ResultDTO.success("成功");
    }

    @Override
    public ResultDTO<List<ActProcdefRespDto>> list(ActProcdefReqDto reqDto) {
        String querySql = new ActProcdef().getQuerySql();
        querySql += " where is_deleted = 0";
        List<ActProcdef> list = super.list(querySql, ActProcdef.class);
        if(CollectionUtils.isEmpty(list)){
            return ResultDTO.success(new ArrayList<>());
        }
        List<ActProcdefRespDto> collect = list.stream().map(it -> {
            ActProcdefRespDto actProcdefRespDto = (ActProcdefRespDto)it.copyProperties(ActProcdefRespDto.class);
            if(!CollectionUtils.isEmpty(it.getExpansionData()) && it.getExpansionData().containsKey("version")){
                actProcdefRespDto.setVersion(String.valueOf(it.getExpansionData().get("version")));
            }
            return actProcdefRespDto;
        }).collect(Collectors.toList());

        return ResultDTO.success(collect);
    }

    @Override
    public ResultDTO updateState(String id) {
        ActProcdef actProcdef = super.getById(id, ActProcdef.class);
        String actNo = actProcdef.getActNo();
        ActProcdef temp = super.selectOne(actProcdef.getQuerySql() + " where is_deleted = 0 and act_no = ? and state = '1' limit 1", actProcdef.getClass(), new Object[]{actProcdef.getActNo()});
        if(Objects.nonNull(temp)){
            return ResultDTO.failed(actNo+": 一个编号只能激活一个, 要想激活当前流程，请先冻结其它相同编号流程");
        }
        if(Objects.nonNull(actProcdef)){
            String state = Objects.equals(actProcdef.getState(), "1") ? "0" : "1";
            ActProcdef n = new ActProcdef();
            n.setId(id);
            n.setState(state);
            super.updateById(n);
        }
        return ResultDTO.success("成功");
    }


    /**
     * 复制流程
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO copy(String id) {
        ActProcdef actProcdef = super.getById(id, ActProcdef.class);
        Map expansionData = actProcdef.getExpansionData();
        if(Objects.isNull(actProcdef)){
            return ResultDTO.failed("不存在的流程");
        }
        if(!expansionData.containsKey("version")){
            expansionData.put("version", new BigDecimal("1.0"));
        }
        BigDecimal version = new BigDecimal(String.valueOf(expansionData.get("version")));
        version = version.add(new BigDecimal("1.0"));
        expansionData.put("version", version);
        ActProcdef newActf = new ActProcdef();
        newActf.copyProperties(actProcdef);
        newActf.setState("0");
        newActf.setExpansionData(expansionData);
        newActf.setId(null);
        String newId = super.insert(newActf);
        String sql = new ActTaskProcdef().getQuerySql() + " where is_deleted = 0 and process_fid = ? order by sort, create_time ";
        List<ActTaskProcdef> list = super.list(sql, ActTaskProcdef.class, new Object[]{id});
        if(!CollectionUtils.isEmpty(list)){
            copyNode(newId, list);
        }
        return ResultDTO.success("成功");
    }


    @Override
    public ResultDTO<ActProcdefRespDto> detail(String id) {
        ActProcdef actProcdef = super.getById(id, ActProcdef.class);
        if(Objects.isNull(actProcdef)){
            return ResultDTO.failed("不存在的数据");
        }
        ActProcdefRespDto actProcdefRespDto = (ActProcdefRespDto) actProcdef.copyProperties(ActProcdefRespDto.class);
        if(!CollectionUtils.isEmpty(actProcdef.getExpansionData()) && actProcdef.getExpansionData().containsKey("version")){
            actProcdefRespDto.setVersion(String.valueOf(actProcdef.getExpansionData().get("version")));
        }
        return ResultDTO.success(actProcdefRespDto);
    }




    private void copyNode(String processId, List<ActTaskProcdef> taskTemplates){
        Map<String, ActTaskProcdef> actTaskProcdefMap = taskTemplates.stream().collect(Collectors.toMap(it -> it.getId(), it -> it));
        Map<String, String> idMapping = new HashMap<>();
        Map<String, ActTaskProcdef> newTaskProcdefMap = new HashMap<>();
        taskTemplates.forEach(it -> {
            ActTaskProcdef actTaskProcdef = new ActTaskProcdef();
            actTaskProcdef.setProcessFid(processId);
            actTaskProcdef.setCond(it.getCond());
            actTaskProcdef.setSort(it.getSort());
            actTaskProcdef.setRemark(it.getRemark());
            actTaskProcdef.setTaskNo(it.getTaskNo());
            actTaskProcdef.setTaskName(it.getTaskName());
            actTaskProcdef.setProcessfName(it.getProcessfName());
            String id = super.insert(actTaskProcdef);
            newTaskProcdefMap.put(id, actTaskProcdef);
            idMapping.put(it.getId(), id);
        });
        idMapping.forEach((oldId, newId) -> {
            ActTaskProcdef oldTask = actTaskProcdefMap.get(oldId);
            ActTaskProcdef newTask = newTaskProcdefMap.get(newId);
            if(StringUtils.isNotEmpty(oldTask.getPreTaskIds())){
                String[] ids = oldTask.getPreTaskIds().split(",");
                List<String> newIds = new LinkedList<>();
                for(String id: ids){
                    newIds.add(idMapping.get(id));
                }
                newTask.setPreTaskIds(newIds.stream().collect(Collectors.joining(",")));
            }

            if(StringUtils.isNotEmpty(oldTask.getNextTaskIds())){
                String[] ids = oldTask.getNextTaskIds().split(",");
                List<String> newIds = new LinkedList<>();
                for(String id: ids){
                    newIds.add(idMapping.get(id));
                }
                newTask.setNextTaskIds(newIds.stream().collect(Collectors.joining(",")));
            }

            if(StringUtils.isNotEmpty(oldTask.getBackTasks())){
                String[] ids = oldTask.getBackTasks().split(",");
                List<String> newIds = new LinkedList<>();
                for(String id: ids){
                    newIds.add(idMapping.get(id));
                }
                newTask.setBackTasks(newIds.stream().collect(Collectors.joining(",")));
            }
            super.updateById(newTask);
        });
    }


}
