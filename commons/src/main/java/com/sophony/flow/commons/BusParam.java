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

    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();
    private BusParam(){
    }

    private static class SingletonHoder{
        private static BusParam instance = new BusParam();
    }


    public static BusParam getInstance(){
        return BusParam.SingletonHoder.instance;
    }

    public ThreadLocal getThreadLocal(){
        return threadLocal;
    }

}
