package com.rabbit.robot.scheduling.crons;

import java.util.concurrent.ScheduledFuture;

/**
 * @author 邢晨旭
 * @date 2020/12/29
 */
public final class ScheduledTask {

    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}