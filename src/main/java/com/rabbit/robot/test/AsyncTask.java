package com.rabbit.robot.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 邢晨旭
 * @date 2021/03/03
 */

@Slf4j
@Component
public class AsyncTask {

//    @Async
    public void doTask1(int i) throws InterruptedException {
        log.info("Task1-Native" + i + " started." + Thread.currentThread().getId());
        Thread.sleep(1000);
    }
//    @Async
    public Boolean doTask2(int i) throws InterruptedException {
        log.info("Task2-Native" + i + " started." + Thread.currentThread().getId());
        return Thread.currentThread().getId()>30;
    }
}
