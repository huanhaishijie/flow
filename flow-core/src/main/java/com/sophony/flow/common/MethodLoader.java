package com.sophony.flow.common;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * MethodLoader
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/6/13 15:37
 */
public class MethodLoader {


    public static Method getMethod(Method[] methods, Class<? extends Annotation> clazz){
        if(methods.length == 0){
            return null;
        }
        for(Method m: methods){
            m.setAccessible(true);
            boolean flag = m.isAnnotationPresent(clazz) || Objects.nonNull(AnnotationUtils.findAnnotation(m, clazz ));
            if(flag){
                return m;
            }
        }
        return null;
    }
}
