package com.sophony.flow.api.reqDto;


import lombok.Data;

/**
 * ActProcdefTagReqDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 19:29
 */

@Data
public class ActProcdefTagReqDto {

    private String id;


    /**
     * 流程模板id
     */
    private String processfId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 0未激活 1激活
     */
    private String state;


}
