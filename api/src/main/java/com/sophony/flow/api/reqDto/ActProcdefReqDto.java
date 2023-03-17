package com.sophony.flow.api.reqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ActProcdefReqDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/8 19:41
 */

@ApiModel(value = "ActProcdefReqDto", description = "流程模板定义请求Dto")
@Data
public class ActProcdefReqDto {



    @ApiModelProperty(name = "id", value = "id")
    private String id;


    /**
     * 流程名称
     */
    @ApiModelProperty(name = "actName", value = "流程名称")
    private String actName;

    /**
     * 流程编号
     */
    @ApiModelProperty(name = "actNo", value = "流程编号")
    @NotBlank(message = "流程编号不能为空")
    private String actNo;

    /**
     * 状态;1.激活/ 0.未激活
     */
    @ApiModelProperty(name = "state", value = "状态;1.激活/ 0.未激活")
    private String state;

    /**
     * 流程描述
     */
    @ApiModelProperty(name = "description", value = "流程描述")
    private String description;










}
