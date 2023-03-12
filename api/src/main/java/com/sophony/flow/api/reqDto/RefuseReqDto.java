package com.sophony.flow.api.reqDto;

import com.sophony.flow.commons.constant.ProcessOperationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * REFUSEReqDto
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/10 0:08
 */

@ApiModel(value = "RefuseReqDto", description = "审核拒绝Dto")
public class RefuseReqDto {



    @ApiModelProperty(name = "processId", value = "流程id")
    @NotBlank(message = "流程id不能为空")
    private String processId;

    ProcessOperationEnum operation = ProcessOperationEnum.REFUSE;


    @ApiModelProperty(name = "content", value = "审核备注/理由")
    private String content;


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


}
