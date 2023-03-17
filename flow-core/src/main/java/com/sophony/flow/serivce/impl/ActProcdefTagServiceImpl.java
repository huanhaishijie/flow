package com.sophony.flow.serivce.impl;

import com.sophony.flow.api.reqDto.ActProcdefTagReqDto;
import com.sophony.flow.api.respDto.ActProcdefTagRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.mapping.ActProcdefTag;
import com.sophony.flow.serivce.IActProcdefTagService;
import com.sophony.flow.worker.common.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ActProcdefTagServiceImpl
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 19:43
 */

@Service
public class ActProcdefTagServiceImpl extends BaseService implements IActProcdefTagService {


    @Override
    @Transactional
    public ResultDTO save(ActProcdefTagReqDto reqDto){
        ActProcdefTag actProcdefTag = new ActProcdefTag();
        BeanUtils.copyProperties(reqDto, actProcdefTag);
        if(StringUtils.isEmpty(actProcdefTag.getId())){
            this.insert(actProcdefTag);
        }
        this.updateById(actProcdefTag);
        return ResultDTO.success("成功");
    }



    public ResultDTO delete(String id){
        ActProcdefTag actProcdefTag = new ActProcdefTag();
        actProcdefTag.setId(id);
        super.deleteById(actProcdefTag);
        return ResultDTO.success("成功");
    }

    @Override
    public ResultDTO<List<ActProcdefTagRespDto>> list(ActProcdefTagReqDto reqDto) {
        String querySql = new ActProcdefTag().getQuerySql();
        querySql += " where is_deleted = 0";
        List<ActProcdefTag> list = super.list(querySql, ActProcdefTag.class);
        if(CollectionUtils.isEmpty(list)){
            return ResultDTO.success(new ArrayList<>());
        }
        List<ActProcdefTagRespDto> collect = list.stream().map(it -> {
            ActProcdefTagRespDto actProcdefTagRespDto = (ActProcdefTagRespDto)it.copyProperties(ActProcdefTagRespDto.class);
            return actProcdefTagRespDto;
        }).collect(Collectors.toList());

        return ResultDTO.success(collect);
    }

    @Override
    public ResultDTO updateState(String id) {
        ActProcdefTag actProcdefTag = super.getById(id, ActProcdefTag.class);
        if(Objects.nonNull(actProcdefTag)){
            String state = Objects.equals(actProcdefTag.getState(), "1") ? "0" : "1";
            ActProcdefTag n = new ActProcdefTag();
            n.setId(id);
            n.setState(state);
            super.updateById(n);
        }
        return ResultDTO.success("成功");
    }




}
