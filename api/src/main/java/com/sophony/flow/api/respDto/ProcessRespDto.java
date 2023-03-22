package com.sophony.flow.api.respDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ProcessRespDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 12:58
 */

@ApiModel(value = "ProcessRespDto", description = "流程实例")
@Data
public class ProcessRespDto{

    @ApiModelProperty(name = "id", value = "id")
    String id;

    /**
     * 流程模板id
     */
    @ApiModelProperty(name = "actId", value = "流程模板id")
    private String actId;


    /**
     * 任务状态
     */
    @ApiModelProperty(name = "state", value = "流程状态")
    private String state;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    @ApiModelProperty(name = "startTime", value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endTime;


}
