package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ActProcdefTagReqDto;
import com.sophony.flow.api.respDto.ActProcdefTagRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IActProcdefTagService;

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
public class ActProcdefTagController {


    @Resource
    IActProcdefTagService actProcdefTagService;


    @PostMapping("save")
    public ResultDTO save(@RequestBody @Validated ActProcdefTagReqDto reqDto) {
        return actProcdefTagService.save(reqDto);
    }



    @GetMapping("delete")
    public ResultDTO save(String id) {
        return actProcdefTagService.delete(id);
    }


    @GetMapping("list")
    public ResultDTO<List<ActProcdefTagRespDto>> list(ActProcdefTagReqDto reqDto) {
        return actProcdefTagService.list(reqDto);
    }


    @GetMapping("updateState")
    public ResultDTO updateState(String id) {
        return actProcdefTagService.updateState(id);
    }



}
