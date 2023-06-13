package com.sophony.flow.common;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AnnotationOpenUtils
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/6/13 23:36
 */
@Component
public class AnnotationOpenUtils {

    @Resource
    Environment environment;


    public boolean isOpen(){
        try {
            return environment.getProperty("yzm.flow.annotation", Boolean.class);
        }catch (Exception e){
            return false;
        }
    }

}
