package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ApproveReqDto;
import com.sophony.flow.api.reqDto.ForcedEndReqDto;
import com.sophony.flow.api.reqDto.RefuseReqDto;
import com.sophony.flow.api.reqDto.WithdrawReqDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
@Api(tags = "流程管理: 审批流操作")
public class OperationController {

    @Resource
    IProcessService processService;






    @PostMapping("/approve")
    @ApiOperation(value = "审核同意", notes = "审核同意")
    public ResultDTO nextProcess(@RequestBody @Valid ApproveReqDto approveReqDto) {
        return processService.approve(approveReqDto);
    }

    @PostMapping("/refuse")
    @ApiOperation(value = "审核拒绝", notes = "审核拒绝")
    public ResultDTO refuseProcess(@RequestBody @Valid RefuseReqDto reqDto) {
        return processService.refuse(reqDto);
    }


    @PostMapping("/withdraw")
    @ApiOperation(value = "撤回", notes = "withdraw")
    public ResultDTO withdrawProcess(@RequestBody @Valid WithdrawReqDto reqDto) {
        return processService.withdraw(reqDto);
    }

    @PostMapping("/forcedEnd")
    @ApiOperation(value = "强制结束", notes = "forcedEnd")
    public ResultDTO forcedEnd(@RequestBody @Valid ForcedEndReqDto reqDto) {
        return processService.forcedEnd(reqDto);
    }


    @GetMapping("getDetail")
    @ApiOperation(value = "获取详情", notes = "获取详情")
    public ResultDTO getDetail(String processId) {
        return processService.getDetail(processId);
    }


    @GetMapping("getTaskHistory")
    @ApiOperation(value = "获取任务历史", notes = "获取任务历史")
    public ResultDTO getTaskHistory(String processId) {
        return processService.getTaskHistory(processId);
    }













}
