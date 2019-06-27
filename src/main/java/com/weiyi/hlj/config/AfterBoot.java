package com.weiyi.hlj.config;

import com.weiyi.hlj.plugins.SchedulerKit;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by qinxy on 2019/6/27.
 * spring 启动之后要执行的东西
 */
@Component
@Order(2)
public class AfterBoot implements ApplicationRunner{

    //日志输出
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchedulerFactory schedulerFactory;

    @Autowired
    private SchedulerKit schedulerKit;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        //运行定时任务
        startScheduler();
        logger.info("开启定时任务");
        schedulerKit.loadDetail();

    }

    private boolean startScheduler(){
        try {
            schedulerFactory.getScheduler().start();
        } catch (SchedulerException e) {
            return false;
        }

        return true;

    }


}
