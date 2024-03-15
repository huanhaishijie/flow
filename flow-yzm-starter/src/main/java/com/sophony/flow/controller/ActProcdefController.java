package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ActProcdefReqDto;
import com.sophony.flow.api.respDto.ActProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IActProcdefService;
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
public class ActProcdefController {

    @Resource
    IActProcdefService actProcdefService;


    @PostMapping("save")
    public ResultDTO save(@RequestBody @Validated ActProcdefReqDto reqDto) {
        return actProcdefService.save(reqDto);
    }



    @GetMapping("delete")
    public ResultDTO delete(String id) {
        return actProcdefService.delete(id);
    }


    @GetMapping("copy")
    public ResultDTO copy(String id) {
        return actProcdefService.copy(id);
    }


    @GetMapping("list")
    public ResultDTO<List<ActProcdefRespDto>> list(ActProcdefReqDto reqDto) {
        return actProcdefService.list(reqDto);
    }


    @GetMapping("detail")
    public ResultDTO<ActProcdefRespDto> detail(String id) {
        return actProcdefService.detail(id);
    }


    @GetMapping("updateState")
    public ResultDTO updateState(String id) {
        return actProcdefService.updateState(id);
    }








}
