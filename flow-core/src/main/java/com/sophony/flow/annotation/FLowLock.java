package com.sophony.flow.annotation;

import java.lang.annotation.*;

/**
 * @ClassName FLowLock
 * @Description 流程锁
 * @Author yzm
 * @Date 2022/12/8 12:00
 * @version 1.5.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FLowLock {


}
