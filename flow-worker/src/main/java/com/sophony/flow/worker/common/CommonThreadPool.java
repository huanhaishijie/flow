package com.sophony.flow.worker.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CommonThreadPool
 * @Description TODO
 * @Author yzm
 * @Date 2023/3/9 9:06
 * @Version 1.0
 */
public class CommonThreadPool {

    /**
     * 线程池对象
     */
    public static ExecutorService pool = null;
    /**
     * 线程池核心池的大小
     */
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 获取当前系统的CPU 数目
     */
    private static int cpuNums = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池的最大线程数
     */
    private static final int MAX_POOL_SIZE = (cpuNums * 2) > CORE_POOL_SIZE ? (cpuNums * 2) : CORE_POOL_SIZE;

    static {
        pool = new ThreadPoolExecutor(
                CORE_POOL_SIZE,             // 核心线程数
                MAX_POOL_SIZE,              // 最大线程数 通常核心线程数=最大线程数 当MAX_POOL_SIZE > CORE_POOL_SIZE 时，并不是当核心线程全部被占用后立马开始创建新的线程（只有在队列也满了之后才会开始新建线程）
                0L,            // 存活时间   >=0 0 永不回收【非核心线程除外】
                TimeUnit.MILLISECONDS,      // 单位
                new ArrayBlockingQueue<Runnable>(100),                          // 队列 存放待执行任务
                new ThreadFactoryBuilder().setNameFormat("CommonThread-%d").build(),    // 创建工厂
                new ThreadPoolExecutor.AbortPolicy());                                  // 拒绝策略 默认拒绝 丢弃 丢弃最老的 主线程执行
    }


}
