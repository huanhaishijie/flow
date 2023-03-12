package com.sophony.flow.serivce;

import com.sophony.flow.api.reqDto.ActTaskProcdefReqDto;
import com.sophony.flow.api.respDto.ActTaskProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;

import java.util.List;

/**
 * ActTaskProcdefService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 20:16
 */
public interface IActTaskProcdefService {

    ResultDTO save(ActTaskProcdefReqDto reqDto);

    ResultDTO delete(String id);

    ResultDTO<ActTaskProcdefRespDto> getById(String id);

    ResultDTO<List<ActTaskProcdefRespDto>> listByActId(String actId);
}
