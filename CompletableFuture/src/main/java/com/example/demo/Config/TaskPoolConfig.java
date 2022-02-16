package com.example.demo.Config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 文件描述
 * 线程池配置
 * @author hjj
 * @date 2020年07月22日 16:19
 */
@Configuration
//@ComponentScan("com.renai.lovepd.web.system.service")
@EnableAsync
@Slf4j
public class TaskPoolConfig implements AsyncConfigurer {

    //参数初始化
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数量大小   注释掉的为标准写法
    //private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int corePoolSize = 1;
    //线程池最大容纳线程数  注释掉的为标准写法
    //private static final int maxPoolSize = CPU_COUNT * 2 + 1;
    private static final int maxPoolSize = 1;
    //阻塞队列
    private static final int workQueue = 20;
    //线程空闲后的存活时长
    private static final int keepAliveTime = 30;

    @Override
    @Bean("asyncTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        //等待队列
        threadPoolTaskExecutor.setQueueCapacity(workQueue);
        //线程前缀
        threadPoolTaskExecutor.setThreadNamePrefix("taskExecutor-");
        //线程池维护线程所允许的空闲时间,单位为秒
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveTime);
        // 线程池对拒绝任务(无线程可用)的处理策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}

