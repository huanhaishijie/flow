package com.sophony.flow.worker.common;

import com.sophony.flow.worker.base.FlowValidService;
import com.sophony.flow.worker.base.PermissionValid;
import org.springframework.context.ApplicationContext;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import com.sophony.flow.worker.base.DataService;

/**
 * FlowBeanFactroy
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 9:06
 */
public class FlowBeanFactory {

    private ApplicationContext applicationContext;

    private AtomicBoolean f = new AtomicBoolean(true);


    private DataService DataService ;


    private FlowValidService flowValidService;

    private FlowBeanFactory(){

    }

    public void shutdown() {
        try {
            applicationContext.getClass().getMethod("close").invoke(applicationContext);
        }catch (Exception exception){
            System.out.println("关闭");
        }

    }

    private static class SingletonHoder{
        private static FlowBeanFactory instance = new FlowBeanFactory();
    }


    public static FlowBeanFactory getInstance(){
        return SingletonHoder.instance;
    }


    public void setApplicationContext(ApplicationContext applicationContext){
        if(f.get()){
            this.applicationContext = applicationContext;
            f.set(false);
        }
    }


    public <T> T getBean(Class<T> clazz){
        if(Objects.nonNull(applicationContext)){
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    public <T> T getBean(String s, Class<T> clazz){
        if(Objects.nonNull(applicationContext)){
            return applicationContext.getBean(s, clazz);
        }
        return null;
    }


    public <T> T getBean(String s){
        if(Objects.nonNull(applicationContext)){
            return (T) applicationContext.getBean(s);
        }
        return null;
    }


    public com.sophony.flow.worker.base.DataService getDataService() {
        return DataService;
    }

    public void setDataService(com.sophony.flow.worker.base.DataService dataService) {
        DataService = dataService;
    }

    public FlowValidService getFlowValidService() {
        return flowValidService;
    }

    public void setFlowValidService(FlowValidService flowValidService) {
        this.flowValidService = flowValidService;
    }

}
