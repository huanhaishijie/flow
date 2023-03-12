package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ActTaskProcdefReqDto;
import com.sophony.flow.api.respDto.ActTaskProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IActTaskProcdefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ActTaskProcdefController
 * @Description TODO
 * @Author yzm
 * @Date 2022/11/15 13:38
 * @Version 1.0
 */
@RestController
@RequestMapping("flow/actTask")
@Api(tags = "流程管理: 流程任务定义")
public class ActTaskProcdefController {

    @Resource
    IActTaskProcdefService actTaskProcdefService;


    @PostMapping("save")
    @ApiOperation(value = "添加和更新流程任务定义", notes = "添加和更新流程任务定义")
    public ResultDTO save(@RequestBody ActTaskProcdefReqDto reqDto) {
        return actTaskProcdefService.save(reqDto);
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除流程任务节点", notes = "删除流程任务节点")
    public ResultDTO delete(@PathVariable("id") String id) {
        return actTaskProcdefService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情", notes = "查询详情")
    public ResultDTO<ActTaskProcdefRespDto> getById(@PathVariable("id") String id) {
        return actTaskProcdefService.getById(id);
    }




    @GetMapping("/list/{actId}")
    @ApiOperation(value = "根据流程id查询节点", notes = "根据流程id查询节点")
    public ResultDTO<List<ActTaskProcdefRespDto>> list(@PathVariable("actId") String actId) {
        return actTaskProcdefService.listByActId(actId);
    }


}
