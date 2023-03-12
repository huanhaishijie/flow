package com.sophony.flow.api.reqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ActProcdefTagReqDto
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 19:29
 */

@ApiModel(value = "ActProcdefTagReqDto", description = "流程模板TagDto")
@Data
public class ActProcdefTagReqDto {

    private String id;


    /**
     * 流程模板id
     */
    @ApiModelProperty(name = "processfId", value = "流程模板id")
    private String processfId;

    /**
     * 标签名称
     */
    @ApiModelProperty(name = "tagName", value = "标签名称")
    private String tagName;

    /**
     * 0未激活 1激活
     */
    @ApiModelProperty(name = "state", value = "0未激活 1激活")
    private String state;


}
