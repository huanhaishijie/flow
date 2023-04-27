package com.sophony.flow.commons.annotation;

import java.lang.annotation.*;

/**
 * @ClassName Browser
 * @Description TODO
 * @Author yzm
 * @Date 2022/12/8 11:58
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowAuditBefore {

    String[] processTemplateIds();

}
