package com.example.demo.controller;


import com.example.demo.Config.ForUserController.CompletableFutureDemo;
import com.example.demo.utils.SmallTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class UserController {


    @Autowired
    private CompletableFutureDemo completableFutureDemo;


    @RequestMapping("/test")
    public String test(Model m) throws ExecutionException, InterruptedException {
        List<String> userNameList = new ArrayList<>();
        for(int i=0;i<30;i++){
            userNameList.add("麻花腾"+i);
        }
        long start =System.currentTimeMillis();
        List<String> userInfoResult = completableFutureDemo.batchGetUserInfoByCompletableFuture(userNameList);
        long end =System.currentTimeMillis();
        SmallTool.printTimeAndThread(userInfoResult+"CompletableFuture耗时--》"+ (end-start)+"ms");
        System.out.println("come in");
        m.addAttribute("status","come in");
        return "index";
    }
}
