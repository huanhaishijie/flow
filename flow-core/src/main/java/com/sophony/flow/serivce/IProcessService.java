package com.sophony.flow.serivce;

import com.sophony.flow.api.reqDto.ApproveReqDto;
import com.sophony.flow.api.reqDto.RefuseReqDto;
import com.sophony.flow.api.reqDto.WithdrawReqDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.commons.model.IProcess;

/**
 * IActProcessService
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程运行实例服务
 * @date 2023/3/9 23:29
 */
public interface IProcessService {

    /**
     * 使用流程模板编号开启流程
     * @param processNo
     * @param process
     * @return
     */
    String start(String processNo, IProcess process);


    /**
     * 审核同意
     * @param approveReqDto
     * @return
     */
    ResultDTO approve(ApproveReqDto approveReqDto);

    /**
     * 审核拒绝
     * @param reqDto
     * @return
     */
    ResultDTO refuse(RefuseReqDto reqDto);

    /**
     * 撤回
     * @param reqDto
     * @return
     */
    ResultDTO withdraw(WithdrawReqDto reqDto);
}
