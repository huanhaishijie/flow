package com.sophony.flow.serivce;

import com.sophony.flow.api.reqDto.ActProcdefReqDto;
import com.sophony.flow.api.respDto.ActProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.mapping.ActProcdef;

import java.util.List;

/**
 * IActProcdefService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/8 19:37
 */
public interface IActProcdefService {

    public ResultDTO save(ActProcdefReqDto reqDto);


    public ResultDTO delete(String id);

    ResultDTO<List<ActProcdefRespDto>> list(ActProcdefReqDto reqDto);

    ResultDTO updateState(String id);

    ResultDTO copy(String id);
}
