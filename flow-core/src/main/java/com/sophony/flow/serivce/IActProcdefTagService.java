package com.sophony.flow.serivce;

import com.sophony.flow.api.reqDto.ActProcdefTagReqDto;
import com.sophony.flow.api.respDto.ActProcdefTagRespDto;
import com.sophony.flow.commons.ResultDTO;

import java.util.List;

/**
 * IActProcdefTag
 *
 * @author yzm
 * @version 1.0
 * @description 流程标签
 * @date 2023/3/9 19:42
 */
public interface IActProcdefTagService {


    public ResultDTO save(ActProcdefTagReqDto reqDto);


    public ResultDTO delete(String id);

    ResultDTO<List<ActProcdefTagRespDto>> list(ActProcdefTagReqDto reqDto);

    ResultDTO updateState(String id);

}
