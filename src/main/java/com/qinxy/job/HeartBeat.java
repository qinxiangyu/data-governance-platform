package com.qinxy.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Created by qinxy on 2019/6/27.
 */
@Component
public class HeartBeat extends AbstractJob {
    @Override
    public void run(JobExecutionContext context) throws JobExecutionException {
        logger.info("心跳，砰...");
    }
}
