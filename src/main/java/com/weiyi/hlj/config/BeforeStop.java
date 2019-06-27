package com.weiyi.hlj.config;

import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by qinxy on 2019/6/27.
 */
public class BeforeStop implements DisposableBean{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchedulerFactory schedulerFactory;
    @Override
    public void destroy() throws Exception {
        //关闭定时任务
        stopScheduler();
        logger.info("关闭定时任务");
    }


    private boolean stopScheduler(){
        try {
            schedulerFactory.getScheduler().shutdown();
        } catch (SchedulerException e) {
            return false;
        }

        return true;

    }
}

