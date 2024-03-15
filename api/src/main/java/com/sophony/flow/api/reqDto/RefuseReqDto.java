package com.sophony.flow.api.reqDto;

import com.sophony.flow.commons.constant.ProcessOperationEnum;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * REFUSEReqDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 0:08
 */

public class RefuseReqDto {



    @NotBlank(message = "流程id不能为空")
    private String processId;

    ProcessOperationEnum operation = ProcessOperationEnum.REFUSE;


    private String content;


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
