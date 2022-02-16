package com.example.demo.test;

import com.example.demo.utils.SmallTool;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class Test2 {
    //异步进阶模型三
    //thenApply  类似于  thenCompose 表示上一个异步任务执行后 连接的下面的操作   默认同步操作
    //小白吃完饭后  付钱  然后叫服务员去给他开发票  同时打电话约朋友一起开黑
    @Test
    public void test_01(){
        SmallTool.printTimeAndThread("小白吃好了");
        SmallTool.printTimeAndThread("小白  结账  要求开发票");
        CompletableFuture<String> invoice = CompletableFuture.supplyAsync(()->{
            SmallTool.printTimeAndThread("服务员收款 500元");
            SmallTool.sleepMillis(100);
            return "500";
            //money为承接上面返回的值  即 "500"
        }).thenApply(money ->{
            SmallTool.printTimeAndThread(String.format("服务员开发票  面额%s元",money));
            SmallTool.sleepMillis(200);
            return String.format("%s元发票",money);

        });
        SmallTool.printTimeAndThread("小白  接到朋友的电话，想一起打游戏");

        SmallTool.printTimeAndThread(String.format("小白拿到%s,准备回家",invoice.join()));
    }

    //异步进阶模型四
    //thenApplyAsync  类似于  thenCompose 表示上一个异步任务执行后  开启另一个线程连接的下面的操作  不和上一个操作在同一个线程
    //个人评测还是失灵时不灵的  不建议用   建议使用thenApply或者thenCompose  然后里面再进行异步操作
    //小白吃完饭后  向A服务员付钱  然后叫B服务员去给他开发票  同时打电话约朋友一起开黑
    @Test
    public void test_02(){
        SmallTool.printTimeAndThread("小白吃好了");
        SmallTool.printTimeAndThread("小白  结账  要求开发票");
        CompletableFuture<String> invoice = CompletableFuture.supplyAsync(()->{
            SmallTool.printTimeAndThread("服务员收款 500元");
            SmallTool.sleepMillis(1000);
            return "500";
            //money为承接上面返回的值  即 "500"
        }).thenApplyAsync(money ->{
            SmallTool.printTimeAndThread(String.format("服务员开发票  面额%s元",money));
            SmallTool.sleepMillis(200);
            return String.format("%s元发票",money);

        });
        SmallTool.printTimeAndThread("小白  接到朋友的电话，想一起打游戏");

        SmallTool.printTimeAndThread(String.format("小白拿到%s,准备回家",invoice.join()));
    }


    //异步进阶模型五
    //applyToEither  左右两边两个任务一起运行  谁先执行完就使用谁的结果
    //小白开完发票后  坐公交回家  700路和800路  谁先到就坐谁
    @Test
    public void test_03(){
        SmallTool.printTimeAndThread("小白走出餐厅，来到公交站");
        SmallTool.printTimeAndThread("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("700路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700路到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("800路公交正在赶来");
            SmallTool.sleepMillis(200);
            return "800路到了";
        }), firstComeBus -> firstComeBus);

        SmallTool.printTimeAndThread(String.format("%s,小白坐车回家", bus.join()));
    }


    //异步进阶模型六
    //exceptionally  可以加在调用链路中间  也可以加在链路后面  整个链路中间有一环出现异常  就会跳到exceptionally中  相对于try{}catch(){}好用
    //小白做700路回家  路上撞树上了  然后小白打车回家
    @Test
    public void test_04(){
        SmallTool.printTimeAndThread("张三走出餐厅，来到公交站");
        SmallTool.printTimeAndThread("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("700路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700路到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("800路公交正在赶来");
            SmallTool.sleepMillis(200);
            return "800路到了";
        }), firstComeBus -> {
            SmallTool.printTimeAndThread(firstComeBus);
            if (firstComeBus.startsWith("700")) {
                throw new RuntimeException("撞树了……");
            }
            return firstComeBus;
        }).exceptionally(e -> {
            SmallTool.printTimeAndThread(e.getMessage());
            SmallTool.printTimeAndThread("小白叫出租车");
            return "出租车 叫到了";
        });

        SmallTool.printTimeAndThread(String.format("%s,小白坐车回家", bus.join()));
    }


}
