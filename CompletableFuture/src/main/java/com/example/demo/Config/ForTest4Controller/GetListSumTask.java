package com.example.demo.Config.ForTest4Controller;

import com.example.demo.utils.SmallTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@Component
@Slf4j
public class GetListSumTask {

    @Async("asyncTaskExecutor")
    public CompletableFuture<Integer> getSum(List<Integer> list) {
        Integer result = 0;
        Integer sum = list.stream().reduce(Integer::sum).orElse(0);
        SmallTool.printTimeAndThread(list.toString());
        SmallTool.printTimeAndThread("总和为："+sum);
        if(sum >5000){
            result =  sum;
        }else {
            result =  0;
        }
        return  CompletableFuture.completedFuture(result);
    }
}
