package com.example.demo.test;

import com.example.demo.utils.SmallTool;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test1 {
    //异步的简单模型
    // 小白点完餐后  去打王者  同时厨师做饭  做完后小白吃饭
    @Test
    public void test_01(){
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            SmallTool.printTimeAndThread("厨师打饭");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋 + 米饭  做好了";
        });
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.sleepMillis(2000);
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s,小白开吃", cf1.join()));
    }
    //异步的进阶模型一  thenCompose  表示上一个异步任务执行后 连接的下面的操作
    //小白点完餐后就去打王者  然后厨师去炒菜  炒完后服务员去乘米饭  然后小白吃
    @Test
    public void test_02() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
            //dish  表示上一个任务的结果  即 dish   = "番茄炒蛋"
        }).thenCompose(dish ->
                CompletableFuture.supplyAsync(() -> {
                    SmallTool.printTimeAndThread("服务员打饭");
                    SmallTool.sleepMillis(100);
                    return dish + " +米饭  做好了";
                })
        );
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.sleepMillis(2000);
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s,小白开吃", cf1.join()));
    }
    //异步的进阶模型二
    // thenCombine  左右的任务同时执行
    //小白点完餐后就去打王者  然后厨师去炒菜  同时服务员开始焖米饭🍚  厨师炒完菜  服务员焖完米饭  然后  服务员打饭  给小白吃
    @Test
    public void test_03() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员蒸饭");
            SmallTool.sleepMillis(1000);
            return "米饭";
        }), (dish, rice) -> {
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(100);
            return String.format("%s + %s 好了", dish, rice);
        });
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.sleepMillis(2000);
        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s,小白开吃", cf1.join()));
    }

    @Test
    public void test_04() throws Exception{
        //自定义一个线程池
        ExecutorService service = Executors.newFixedThreadPool(10);
//        CompletableFuture的使用

        //1.无返回值的异步任务 runAsync()
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
        }, service);

        //2.有返回值异步任务 supplyAsync()
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
            return i;
        }, service).whenComplete((r, e) -> {
//            whenComplete第一个参数是结果，第二个参数是异常，他可以感知异常，无法返回默认数据
            System.out.println("执行完毕，结果是---" + r + "异常是----" + e);
        }).exceptionally(u -> {
//            exceptionally只有一个参数是异常类型，他可以感知异常，同时返回默认数据10
            return 10;
//            handler既可以感知异常，也可以返回默认数据，是whenComplete和exceptionally的结合
        }).handle((r, e) -> {
            if (r != null) {
                return r;
            }
            if (e != null) {
                return 0;
            }
            return 0;
        });

    }

}
