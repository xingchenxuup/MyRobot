package com.rabbit.robot;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.rabbit.robot.config.TaskThreadPoolConfig;
import com.rabbit.robot.helper.ApplicationContextHelper;
import com.rabbit.robot.listener.*;
import com.rabbit.robot.star.RobotStar;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableConfigurationProperties({TaskThreadPoolConfig.class} )
@SpringBootApplication
@EnableScheduling
@MapperScan("com.rabbit.robot.mapper")
public class RobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(RobotApplication.class, args);

        // Mirai启动
        RobotStar.star(ApplicationContextHelper.getBean(FriendListener.class),
                ApplicationContextHelper.getBean(GroupListener.class),
                ApplicationContextHelper.getBean(GroupRequestListener.class),
                ApplicationContextHelper.getBean(GroupTalkativeChangeListener.class),
                ApplicationContextHelper.getBean(LoginListener.class));
    }


    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }


}
