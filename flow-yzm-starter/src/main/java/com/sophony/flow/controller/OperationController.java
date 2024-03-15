package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ApproveReqDto;
import com.sophony.flow.api.reqDto.RefuseReqDto;
import com.sophony.flow.api.reqDto.WithdrawReqDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IProcessService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * OperationController
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 10:31
 */
@RestController
@RequestMapping("flow/operation")
public class OperationController {

    @Resource
    IProcessService processService;






    @PostMapping("/approve")
    public ResultDTO nextProcess(@RequestBody @Valid ApproveReqDto approveReqDto) {
        return processService.approve(approveReqDto);
    }


    @PostMapping("/batchApprove")
    public ResultDTO batchApprove(@RequestBody @Valid List<ApproveReqDto> approveReqDtos) {
        return processService.batchApprove(approveReqDtos);
    }

    @PostMapping("/refuse")
    public ResultDTO refuseProcess(@RequestBody @Valid RefuseReqDto reqDto) {
        return processService.refuse(reqDto);
    }


    @PostMapping("/batchRefuse")
    public ResultDTO batchRefuse(@RequestBody @Valid List<RefuseReqDto> approveReqDtos) {
        return processService.batchRefuse(approveReqDtos);
    }


    @PostMapping("/withdraw")
    public ResultDTO withdrawProcess(@RequestBody @Valid WithdrawReqDto reqDto) {
        return processService.withdraw(reqDto);
    }


    @GetMapping("getDetail")
    public ResultDTO getDetail(String processId) {
        return processService.getDetail(processId);
    }


    @GetMapping("getTaskHistory")
    public ResultDTO getTaskHistory(String processId) {
        return processService.getTaskHistory(processId);
    }













}
