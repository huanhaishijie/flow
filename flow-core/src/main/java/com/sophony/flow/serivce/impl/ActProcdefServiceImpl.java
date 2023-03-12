package com.sophony.flow.serivce.impl;

import com.sophony.flow.api.reqDto.ActProcdefReqDto;
import com.sophony.flow.api.respDto.ActProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.mapping.ActProcdef;
import com.sophony.flow.serivce.IActProcdefService;
import com.sophony.flow.worker.common.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ActProcdefImpl
 *
 * @author yzm
 * @version 1.0
 * @description 流程模板定义实现
 * @date 2023/3/8 19:36
 */
@Service
public class ActProcdefServiceImpl extends BaseService implements IActProcdefService {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ResultDTO save(ActProcdefReqDto reqDto){

        ActProcdef actProcdef = new ActProcdef();
        String sql = actProcdef.getQuerySql() + " where act_no = ? and is_deleted = 0 limit 1";
        ActProcdef res = super.selectOne(sql, ActProcdef.class , new Object[]{reqDto.getActNo()});
        if(Objects.nonNull(res)){
            return ResultDTO.failed(reqDto.getActNo()+": 当前编号模板已经存在， 请重新命名");
        }
        BeanUtils.copyProperties(reqDto, actProcdef);
        if(StringUtils.isEmpty(actProcdef.getId())){
            this.insert(actProcdef);
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
            return actProcdefRespDto;
        }).collect(Collectors.toList());

        return ResultDTO.success(collect);
    }

    @Override
    public ResultDTO updateState(String id) {
        ActProcdef actProcdef = super.getById(id, ActProcdef.class);
        if(Objects.nonNull(actProcdef)){
            String state = Objects.equals(actProcdef.getState(), "1") ? "0" : "1";
            ActProcdef n = new ActProcdef();
            n.setId(id);
            n.setState(state);
            super.updateById(n);
        }
        return ResultDTO.success("成功");
    }


}
