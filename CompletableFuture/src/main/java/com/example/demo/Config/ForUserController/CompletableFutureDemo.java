package com.example.demo.Config.ForUserController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 文件描述
 *
 * @author hjj
 * @date 2020年07月22日 16:48
 */
@Component
@Slf4j
public class CompletableFutureDemo {
    @Autowired
    private AsyncTask asyncTask;

    public List<String> batchGetUserInfoByCompletableFuture(List<String> userNameList) throws InterruptedException, ExecutionException {
        List<CompletableFuture<String>> userInfoFutrues = userNameList.stream().map(userName -> asyncTask.getUserInfoByCompletableFuture(userName)).collect(Collectors.toList());
        return userInfoFutrues.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }
}
