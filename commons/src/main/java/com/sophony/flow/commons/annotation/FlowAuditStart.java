package com.sophony.flow.commons.annotation;

/**
 * FlowAuditStart
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/6/13 16:45
 */
public @interface FlowAuditStart {

    /**
     * 匹配模板， 不填默认全匹配
     * @return
     */
    String[] processTemplateIds();


}
