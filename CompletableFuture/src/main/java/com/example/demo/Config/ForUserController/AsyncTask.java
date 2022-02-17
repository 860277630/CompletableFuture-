package com.example.demo.Config.ForUserController;

import com.example.demo.utils.SmallTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 文件描述
 * 任务异步处理
 *
 *
 * @date 2020年07月22日 16:33
 */
@Component
@Slf4j
public class AsyncTask {

    @Async("asyncTaskExecutor")
    public CompletableFuture<String> getUserInfoByCompletableFuture(String userName) {
        String userInfo="";
        try{
            Thread.sleep(10);
            userInfo = userName+"的基本信息！";
            SmallTool.printTimeAndThread(userInfo);

        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(userInfo);
    }

}

