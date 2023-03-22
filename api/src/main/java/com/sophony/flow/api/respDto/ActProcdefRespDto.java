package com.sophony.flow.api.respDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ActProcdefRespDto
 * @Description TODO
 * @Author yzm
 * @Date 2022/11/9 15:33
 * @version 1.5.0
 */

@ApiModel(value = "ActProcdefRespDto", description = "流程定义响应DTO")
@Data
public class ActProcdefRespDto {

    @ApiModelProperty(name = "id", value = "id")
    private String id;

    @ApiModelProperty(name = "actName", value = "流程名称")
    private String actName;

    @ApiModelProperty(name = "actNo", value = "流程编号")
    private String actNo;

    @ApiModelProperty(name = "state", value = "状态;1.激活/ 0.未激活")
    private String state;

    @ApiModelProperty(name = "description", value = "流程简介")
    private String description;

    @ApiModelProperty(name = "version", value = "版本")
    private String version;

}
