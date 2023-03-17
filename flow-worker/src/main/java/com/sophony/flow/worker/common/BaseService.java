package com.sophony.flow.worker.common;


import com.sophony.flow.absEo.BaseMappingEO;
import com.sophony.flow.mapping.ActTaskProcdef;
import com.sophony.flow.worker.modle.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * BaseService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 0:17
 */
public abstract class BaseService{

    FlowBeanFactory flowBeanFactory = FlowBeanFactory.getInstance();




    public String insert(BaseMappingEO eo) {
        return flowBeanFactory.getDataService().insert(eo);
    }


    public void updateById(BaseMappingEO eo) {
        flowBeanFactory.getDataService().updateById(eo);
    }


    public void deleteById(BaseMappingEO eo) {
        flowBeanFactory.getDataService().deleteById(eo);
    }


    public <T extends BaseMappingEO> List<T> list(String sql, Class<T> rClass){
        return flowBeanFactory.getDataService().list(sql, rClass);
    }

    public <T extends BaseMappingEO> List<T> list(String sql, Class<T> rClass, Object[] args){
        return flowBeanFactory.getDataService().list(sql, rClass, args);
    }


    public <T extends BaseMappingEO> T getById(String id, Class<T> rClass){
        return (T) flowBeanFactory.getDataService().getById(id, rClass);
    }


    public User getCurrentUser(){
        return  flowBeanFactory.getDataService().getCurrentUser();
    }


    public String conditionByIn(String ids, Class<?> clazz){
        if(StringUtils.isEmpty(ids)){
            return "";
        }
        if(Objects.equals(clazz, Integer.class) || Objects.equals(clazz, Long.class)){
            return " ("+ids+") ";
        }
        return " ('" + Arrays.asList(ids.split(",")).stream().collect(Collectors.joining("','")) +"') ";

    }

    protected <T extends BaseMappingEO> List<T> selectByIds(String ids, Class<T> tClass) {
        return  flowBeanFactory.getDataService().selectByIds(ids, tClass);
    }

    public <T extends BaseMappingEO> T selectOne(String sql, Class<T> tClass, Object[] args){
        return (T) flowBeanFactory.getDataService().selectOne(sql, tClass, args);
    }

}
