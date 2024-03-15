package com.sophony.flow.controller;

import com.sophony.flow.api.reqDto.ActTaskProcdefReqDto;
import com.sophony.flow.api.respDto.ActTaskProcdefRespDto;
import com.sophony.flow.commons.ResultDTO;
import com.sophony.flow.serivce.IActTaskProcdefService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName ActTaskProcdefController
 * @Description TODO
 * @Author yzm
 * @Date 2022/11/15 13:38
 * @version 1.5.0
 */
@RestController
@RequestMapping("flow/actTask")
public class ActTaskProcdefController {

    @Resource
    IActTaskProcdefService actTaskProcdefService;


    @PostMapping("save")
    public ResultDTO save(@RequestBody @Valid ActTaskProcdefReqDto reqDto) {
        return actTaskProcdefService.save(reqDto);
    }

    @PostMapping("delete/{id}")
    public ResultDTO delete(@PathVariable("id") String id) {
        return actTaskProcdefService.delete(id);
    }

    @GetMapping("/{id}")
    public ResultDTO<ActTaskProcdefRespDto> getById(@PathVariable("id") String id) {
        return actTaskProcdefService.getById(id);
    }




    @GetMapping("/list/{actId}")
    public ResultDTO<List<ActTaskProcdefRespDto>> list(@PathVariable("actId") String actId) {
        return actTaskProcdefService.listByActId(actId);
    }


}
