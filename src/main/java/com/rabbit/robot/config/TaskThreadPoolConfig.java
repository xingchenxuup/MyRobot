package com.rabbit.robot.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置类
 *
 * @author 邢晨旭
 * @date 2021/03/03
 */
@ConfigurationProperties(prefix = "task.pool")
@Getter
@Setter
public class TaskThreadPoolConfig {
    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveSeconds;

    private int queueCapacity;

}

