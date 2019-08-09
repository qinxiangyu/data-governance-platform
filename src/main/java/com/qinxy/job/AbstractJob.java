package com.qinxy.job;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by qinxy on 2019/6/27.
 */
public abstract class AbstractJob implements Job {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        String jobGroup = context.getJobDetail().getKey().getGroup();
        String jobId = context.getJobDetail().getJobDataMap().getString("_id");
        StringBuilder startJob = new StringBuilder("start-job[");
        startJob.append("job_id:");
        startJob.append(jobId);
        startJob.append("\tname:");
        startJob.append(jobName);
        startJob.append("\tgroup:");
        startJob.append(jobGroup);
        startJob.append("]");
        logger.info(startJob.toString());
        String triggerName = context.getTrigger().getKey().getName();
        String triggerGroup = context.getTrigger().getKey().getGroup();
        StringBuilder trigger = new StringBuilder("start-job-for-tarigger[");
        trigger.append("name:");
        trigger.append(triggerName);
        trigger.append("group:");
        trigger.append(triggerGroup);
        trigger.append("]");
        logger.info(trigger.toString());
        logger.info("job-current-time:{}", DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        run(context);
        StringBuilder endJob = new StringBuilder("end-job[");
        endJob.append("id:");
        endJob.append(jobId);
        endJob.append("\tname:");
        endJob.append(jobName);
        endJob.append("\tgroup:");
        endJob.append(jobGroup);
        endJob.append("]");
        logger.info(endJob.toString());
    }

    /**
     * 自定义任务逻辑
     * @param context
     * @throws JobExecutionException
     */
    public abstract void run(JobExecutionContext context) throws JobExecutionException;
}
