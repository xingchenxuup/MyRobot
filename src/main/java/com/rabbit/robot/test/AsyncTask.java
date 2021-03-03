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

    @Async
    public void doTask(int i) throws InterruptedException {
        log.info("Task-Native" + i + " started." + Thread.currentThread().getId());
    }
}
