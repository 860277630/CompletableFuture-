package com.example.demo.controller;


import com.example.demo.Config.ForTest4Controller.GetListSumTask;
import com.example.demo.Config.ForUserController.CompletableFutureDemo;
import com.example.demo.utils.ListUtils;
import com.example.demo.utils.SmallTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//并发操作  有返回值情况   结合spring @Async来做
//因为test类中使用的是  runasync来进行异步
// 此类来结合  @async来来进行使用
@Controller
public class Test4Controller {

    @Autowired
    private GetListSumTask getListSumTask;

    private static final Integer QUERY_NUM = 10;
    private static final Float Z_FACTOR = 0.4F;
    //  实际应用场景
    //  list  每10个一次操作  累加总数超过5000的片段

    @RequestMapping("/spiltList")
    public String test(Model m)  {

        //  获取一个长度随机的  list
        List<Integer> list = ListUtils.randomList((int) (Math.random() * 1000));
        //List<Integer> list = ListUtils.AscList(90);
        int size = list.size();
        SmallTool.printTimeAndThread("列表长度为："+size);

        int spiltNum = (size/QUERY_NUM)+(size%QUERY_NUM==0?(size>QUERY_NUM?0:1):0)+((float)(size%QUERY_NUM)/(float)QUERY_NUM>Z_FACTOR?1:0);

        SmallTool.printTimeAndThread("查询次数为："+spiltNum);

        List<CompletableFuture> lists = new ArrayList<>();

        for(int i = 0; i<spiltNum ; i++){
            if(i == spiltNum - 1){
                lists.add(getListSumTask.getSum(list.subList(i*QUERY_NUM,size))) ;
            }else {
                lists.add(getListSumTask.getSum(list.subList(i*QUERY_NUM,(i+1)*QUERY_NUM))) ;
            }
        }

        CompletableFuture.allOf(lists.toArray(new CompletableFuture[lists.size()])).join();

        //到了主线程后输出总数
        lists.forEach(x->{
            try {
                System.out.println("每个节点的数据为："+x.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        SmallTool.printTimeAndThread("所有列表都处理完了 ");
        System.out.println("come in");
        m.addAttribute("status","come in");
        return "index";
    }


}
