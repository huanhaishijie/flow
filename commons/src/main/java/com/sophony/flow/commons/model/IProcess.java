package com.sophony.flow.commons.model;

/**
 * IProcess
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 23:33
 */
public interface IProcess<T extends IProcess> {


    default T construction(){
        return null;
    }

    /**
     * 异步调用
     * @param t
     */
    default void auditAfter(T t){
        System.out.println("审核流程开始回调接口");
    }

    /**
     * 同步调用
     * @param t
     * @return
     */
    default boolean auditBefore(T t){
        System.out.println("流程审核后回调");
        return true;
    }


    /**
     * 异步调用
     * @param t
     */
    default void goEndBack(T t){
        System.out.println("审核流程结束回调接口");
    }



    /**
     * 流程开启回调
     * @param t
     */
    default void start(T t){
        System.out.println("流程开启回调");
    }










}
