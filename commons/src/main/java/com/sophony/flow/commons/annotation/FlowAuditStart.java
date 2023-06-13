package com.sophony.flow.commons.annotation;

import java.lang.annotation.*;

/**
 * FlowAuditStart
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/6/13 16:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowAuditStart {

    /**
     * 匹配模板， 不填默认全匹配
     * @return
     */
    String[] processTemplateIds() default {};


}
