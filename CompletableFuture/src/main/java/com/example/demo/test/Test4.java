package com.example.demo.test;


import com.example.demo.utils.ListUtils;
import com.example.demo.utils.SmallTool;
import org.junit.Test;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//并发操作   有返回值情况

//list  每10个一次操作  统计   和超过5000  的list片段数
public class Test4 {
    private static final Integer QUERY_NUM = 10;
    private static final Float Z_FACTOR = 0.4F;

    @Test
    public void test_01(){
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
        //到了主线程后输出总数
        lists.forEach(x->{
            try {
                System.out.println("list中超过2000的数据节点为："+x.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        SmallTool.printTimeAndThread("所有列表都处理完了 ");

    }


    private CompletableFuture<Integer> dealList(List<Integer> list,ExecutorService threadPool){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            Integer sum = list.stream().reduce(Integer::sum).orElse(0);
            SmallTool.printTimeAndThread(list.toString()+"==总和为：=="+sum);
            if(sum >2000){
                return sum;
            }else {
                return 0;
            }
        }, threadPool);
        return future;
    }

}
