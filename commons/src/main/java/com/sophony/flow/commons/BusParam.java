package com.sophony.flow.commons;

import java.util.Map;

/**
 * BusParam
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/17 16:07
 */
public class BusParam {
    String sqlType;

    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();
    private BusParam(){
    }

    private static class SingletonHolder{
        private static BusParam instance = new BusParam();
    }


    public static BusParam getInstance(){
        return BusParam.SingletonHolder.instance;
    }

    public void setMap(Map<String, Object> map){
        if(map.containsKey("sqlType")){
            sqlType = String.valueOf(map.get("sqlType"));
        }
        threadLocal.set(map);
    }

    public Map<String, Object> getMap(){
        return threadLocal.get();
    }

    public String getSqlType() {
        return sqlType;
    }


}
