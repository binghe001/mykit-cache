package io.mykit.cache.redis.spring.annotation.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:59
 * @description 线程任务工具类
 */
public class ThreadTaskAnnotationUtils {

    private static ThreadPoolTaskExecutor taskExecutor = null;

    static {
        taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(5);
        // 最大线程数
        taskExecutor.setMaxPoolSize(50);
        // 队列最大长度
        taskExecutor.setQueueCapacity(1000);
        // 线程池维护线程所允许的空闲时间(单位秒)
        taskExecutor.setKeepAliveSeconds(120);
        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        taskExecutor.initialize();
    }

    public static void run(Runnable runnable) {
        taskExecutor.execute(runnable);
    }
}
