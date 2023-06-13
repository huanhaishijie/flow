package com.sophony.flow.worker.common;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * FlowClassLoader
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/6/13 14:48
 */
public class FlowClassLoader {
    public static Object flowClassLoader(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if(Objects.isNull(clazz)){
            return null;
        }
        Object instance = clazz.newInstance();
        Field[] fields1 = clazz.getFields();
        Field[] fields2 = clazz.getDeclaredFields();
        assignment(instance, fields1);
        assignment(instance, fields2);
        return instance;
    }

    private static void assignment(Object instance, Field[] fields) throws IllegalAccessException {
        if(fields.length == 0){
            return;
        }
        for(Field f: fields){
            f.setAccessible(true);
            boolean flag = f.isAnnotationPresent(Resource.class) || f.isAnnotationPresent(Autowired.class);
            if(flag){
                f.set(instance, FlowBeanFactory.getInstance().getBean(f.getType()));
            }
        }
    }

}
