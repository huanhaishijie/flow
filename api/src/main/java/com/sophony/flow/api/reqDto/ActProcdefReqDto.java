package com.sophony.flow.api.reqDto;


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

@Data
public class ActProcdefReqDto {



    private String id;


    /**
     * 流程名称
     */
    private String actName;

    /**
     * 流程编号
     */
    @NotBlank(message = "流程编号不能为空")
    private String actNo;

    /**
     * 状态;1.激活/ 0.未激活
     */
    private String state;

    /**
     * 流程描述
     */
    private String description;










}
