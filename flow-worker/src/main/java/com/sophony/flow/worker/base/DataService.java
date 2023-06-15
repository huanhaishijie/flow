package com.sophony.flow.worker.base;

import com.sophony.flow.absEo.BaseMappingEO;
import com.sophony.flow.worker.modle.User;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DataService
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 10:26
 */
public interface DataService<T, R extends BaseMappingEO> {



    String insert(R r);

    void updateById(R r);

    void deleteById(R r);


    <T extends BaseMappingEO> T getById(String id, Class<R> rClass);

    <T extends BaseMappingEO> List<T> list(String sql,  Class<R> rClass);

    <T extends BaseMappingEO> List<T> list(String sql,  Class<R> rClass, Object[] args);

    User getCurrentUser();

    <T extends BaseMappingEO> List<T> selectByIds(String nextTaskIds, Class<T> tClass);


    public default String conditionByIn(String ids, Class<?> clazz){
        if(StringUtils.isEmpty(ids)){
            return "";
        }
        if(Objects.equals(clazz, Integer.class) || Objects.equals(clazz, Long.class)){
            return " ("+ids+") ";
        }

        if(ids.trim().split(",").length == 0){
            return "";
        }
        String prefix = " (";
        for(String param : ids.split(",")){
            prefix +="?,";
        }
        String sqlContent = prefix.substring(0, prefix.length() -1 ) +") ";
        return sqlContent;
    }

    <T extends BaseMappingEO> T selectOne(String sql, Class<T> tClass, Object[] args);

}
