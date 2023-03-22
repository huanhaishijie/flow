package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ActProcdefReqDto;
import com.sophony.flow.api.respDto.ActProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IActProcdefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ActProcdefController
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/8 22:03
 */

@RestController
@RequestMapping("flow/actProcdef")
@Api(tags = "流程管理:流程定义和管理")
public class ActProcdefController {

    @Resource
    IActProcdefService actProcdefService;


    @PostMapping("save")
    @ApiOperation(value = "添加和更新流程定义", notes = "添加和更新流程定义")
    public ResultDTO save(@RequestBody @Validated ActProcdefReqDto reqDto) {
        return actProcdefService.save(reqDto);
    }



    @GetMapping("delete")
    @ApiOperation(value = "删除流程定义", notes = "删除流程定义")
    public ResultDTO delete(String id) {
        return actProcdefService.delete(id);
    }


    @GetMapping("copy")
    @ApiOperation(value = "复制整个流程", notes = "复制整个流程")
    public ResultDTO copy(String id) {
        return actProcdefService.copy(id);
    }


    @GetMapping("list")
    @ApiOperation(value = "获取list", notes = "获取list")
    public ResultDTO<List<ActProcdefRespDto>> list(ActProcdefReqDto reqDto) {
        return actProcdefService.list(reqDto);
    }


    @GetMapping("detail")
    @ApiOperation(value = "获取详情", notes = "获取详情")
    public ResultDTO<ActProcdefRespDto> detail(String id) {
        return actProcdefService.detail(id);
    }


    @GetMapping("updateState")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    public ResultDTO updateState(String id) {
        return actProcdefService.updateState(id);
    }








}
