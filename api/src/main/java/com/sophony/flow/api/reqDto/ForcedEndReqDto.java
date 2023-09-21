package com.sophony.flow.api.reqDto;

import com.sophony.flow.commons.constant.ProcessOperationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;


/**
 * WITHDRAWReqDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 0:09
 */
@ApiModel(value = "ForcedEndReqDto", description = "强制结束Dto")
public class ForcedEndReqDto {

    @ApiModelProperty(name = "processId", value = "流程id")
    @NotBlank(message = "流程id不能为空")
    private String processId;

    ProcessOperationEnum operation = ProcessOperationEnum.FORCEDEND;


    @ApiModelProperty(name = "content", value = "审核备注/理由")
    private String content;

    @ApiModelProperty(name = "otherParam", value = "审核拓展参数")
    private String otherParam;



    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ProcessOperationEnum getOperation() {
        return operation;
    }

    public String getOtherParam() {
        return otherParam;
    }

    public void setOtherParam(String otherParam) {
        this.otherParam = otherParam;
    }
}
