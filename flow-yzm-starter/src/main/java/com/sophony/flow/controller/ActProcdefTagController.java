package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ActProcdefTagReqDto;
import com.sophony.flow.api.respDto.ActProcdefTagRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IActProcdefTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ActProcdefTagController
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 19:41
 */

@RestController
@RequestMapping("flow/Tag")
@Api(tags = "流程管理: 流程标签管理")
public class ActProcdefTagController {


    @Resource
    IActProcdefTagService actProcdefTagService;


    @PostMapping("save")
    @ApiOperation(value = "添加和更新流程标签", notes = "添加和更新流程标签")
    public ResultDTO save(@RequestBody @Validated ActProcdefTagReqDto reqDto) {
        return actProcdefTagService.save(reqDto);
    }



    @GetMapping("delete")
    @ApiOperation(value = "删除流程标签", notes = "删除流程标签")
    public ResultDTO save(String id) {
        return actProcdefTagService.delete(id);
    }


    @GetMapping("list")
    @ApiOperation(value = "获取list", notes = "获取list")
    public ResultDTO<List<ActProcdefTagRespDto>> list(ActProcdefTagReqDto reqDto) {
        return actProcdefTagService.list(reqDto);
    }


    @GetMapping("updateState")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    public ResultDTO updateState(String id) {
        return actProcdefTagService.updateState(id);
    }



}
