package com.example.demo.test;

import com.example.demo.utils.ListUtils;
import com.example.demo.utils.SmallTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//并发操作  无返回值情况

public class Test3 {

    private static final Integer QUERY_NUM = 10;
    private static final Float Z_FACTOR = 0.4F;

    //  实际应用场景
    //  一个长度随机的list  每10个为1份  最终偏差因子为0.1  然后异步存入数据库中  然后输出语句：数据存储成功
    @Test
    public void test_01() {
        //  获取一个长度随机的  list
        List<Integer> list = ListUtils.AscList((int) (Math.random() * 1000));
        //List<Integer> list = ListUtils.AscList(90);
        int size = list.size();
        SmallTool.printTimeAndThread("列表长度为："+size);

        int spiltNum = (size/QUERY_NUM)+(size%QUERY_NUM==0?(size>QUERY_NUM?0:1):0)+((float)(size%QUERY_NUM)/(float)QUERY_NUM>Z_FACTOR?1:0);

        SmallTool.printTimeAndThread("查询次数为："+spiltNum);

        ExecutorService threadPool = Executors.newCachedThreadPool();

        List<CompletableFuture> lists = new ArrayList<>();

        for(int i = 0; i<spiltNum ; i++){
            if(i == spiltNum - 1){
                lists.add(dealList(list.subList(i*QUERY_NUM,size),threadPool)) ;
            }else {
                lists.add(dealList(list.subList(i*QUERY_NUM,(i+1)*QUERY_NUM),threadPool)) ;
            }
        }

        CompletableFuture.allOf(lists.toArray(new CompletableFuture[lists.size()])).join();

        threadPool.shutdown();

        SmallTool.printTimeAndThread("所有列表都处理完了 ");

    }


    private CompletableFuture<Void> dealList(List<Integer> list,ExecutorService threadPool){
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            SmallTool.printTimeAndThread(list.toString());
        }, threadPool);
        return future;
    }
}
