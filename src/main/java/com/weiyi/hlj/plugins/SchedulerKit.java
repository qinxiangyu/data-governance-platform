package com.weiyi.hlj.plugins;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.weiyi.hlj.entity.ScheduleJob;
import com.weiyi.hlj.entity.ScheduleTrigger;
import com.weiyi.hlj.job.AbstractJob;
import com.weiyi.hlj.service.ScheduleJobService;
import com.weiyi.hlj.service.ScheduleTriggerService;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinxy on 2019/6/27.
 */
@Component
public class SchedulerKit {

    private Logger logger = LoggerFactory.getLogger(SchedulerKit.class);

    private static SchedulerKit instance = null;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private ScheduleTriggerService scheduleTriggerService;

    /**
     * 加载任务
     */
    public void loadDetail() {
        List<ScheduleJob> jobs = scheduleJobService.enableList();
        if(jobs == null || jobs.isEmpty()){
            logger.warn("not found any jobs.");
            return;
        }
        jobs.forEach((ScheduleJob job) -> {
            if (checkExists(jobKey(job))) {
                cancelJob(job);
            } else {
                scheduleJob(job);
            }
        });
    }


    /**
     * 取消任务
     * @param job
     * @return
     */
    public boolean cancelJob(ScheduleJob job){
        if (checkExists(jobKey(job))) {
            List<? extends Trigger> triggers = getTriggersOfJob(jobKey(job));
            if(triggers != null && !triggers.isEmpty()){
                triggers.forEach((Trigger t) -> {
                    unScheduleJob(t.getKey());
                });
            }
            deleteJob(jobKey(job));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 启动任务
     * @param job
     * @return
     */
    public boolean scheduleJob(ScheduleJob job){
        if(checkExists(jobKey(job))){
            logger.warn("job-exist:" + JSONObject.toJSONString(job));
            return false;
        }

        ScheduleTrigger scheduleTrigger = scheduleTriggerService.getById(job.getTriggerId());
        if(scheduleTrigger == null && scheduleTrigger.getId() == null){
            logger.warn("job-config-error:" + JSONObject.toJSONString(job));
        } else {
            Class clazz = loadClass(job.getJobImpl());
            Map<String,String> param = new HashMap<>();
            if(clazz == null){
                logger.warn("ClassNotFound:" + job.getJobImpl());
                return false;
            }
            if(!ClassUtils.isAssignable(clazz, AbstractJob.class)){
                logger.warn("JobInvalid:" + job.getJobImpl());
                return false;
            }
            param.put("_job_name",job.getJobName());
            param.put("_job_group",job.getJobGroup());
            param.put("_id",String.valueOf(job.getId()));
            if(!StringUtils.isBlank(job.getJobParams())){
                JSONObject params =  parseObject(job.getJobParams());
                if(params != null){
                    params.keySet().forEach((String key) -> {
                        param.put(key,params.getString(key));
                    });
                }
            }
            JobDetail jobDetail = JobBuilder.newJob(clazz).
                    withIdentity(job.getJobName(), job.getJobGroup()).usingJobData(new JobDataMap(param)).build();

            Trigger trigger = TriggerBuilder.newTrigger().
                    withIdentity(scheduleTrigger.getTriggerName(), scheduleTrigger.getTriggerGroup())
                    .startNow().
                            withSchedule(builder(scheduleTrigger)).build();
            scheduleJob(jobDetail, trigger);
            return true;
        }
        return false;
    }

    private JSONObject parseObject(String text){
        try {
            return JSONObject.parseObject(text);
        } catch (JSONException e){
            logger.error("JSONException:",e);
        }
        return null;
    }

    private ScheduleBuilder builder(ScheduleTrigger scheduleTrigger){
        if(scheduleTrigger == null){
            return null;
        }
        switch (scheduleTrigger.getTriggerType()){
            case 0 :
                return CronScheduleBuilder.cronSchedule(scheduleTrigger.getCronExpression());
            case 1 :
                if(scheduleTrigger.getTriggerCount() == 0){
                    return SimpleScheduleBuilder.repeatHourlyForever(scheduleTrigger.getTriggerInterval());
                } else {
                    return SimpleScheduleBuilder.repeatHourlyForTotalCount(
                            scheduleTrigger.getTriggerCount(),scheduleTrigger.getTriggerInterval());
                }
            case 2 :
                if(scheduleTrigger.getTriggerCount() == 0){
                    return SimpleScheduleBuilder.repeatMinutelyForever(scheduleTrigger.getTriggerInterval());
                } else {
                    return SimpleScheduleBuilder.repeatMinutelyForTotalCount(
                            scheduleTrigger.getTriggerCount(),scheduleTrigger.getTriggerInterval());
                }
            case 3 :
                if(scheduleTrigger.getTriggerCount() == 0){
                    return SimpleScheduleBuilder.repeatSecondlyForever(scheduleTrigger.getTriggerInterval());
                } else {
                    return SimpleScheduleBuilder.repeatSecondlyForTotalCount(
                            scheduleTrigger.getTriggerCount(),scheduleTrigger.getTriggerInterval());
                }
        }
        return null;
    }

    private Class loadClass(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e){
            logger.error("ClassNotFoundException:" + name);
        }
        return null;
    }


    private boolean deleteJob(JobKey jobKey){
        try {
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException e){
            logger.error("delete-job-exception:name-" + jobKey.getName() + ",group-" + jobKey.getGroup());
            logger.error("SchedulerException:", e);
        }
        return false;
    }

    private Date scheduleJob(JobDetail jobDetail, Trigger trigger){
        try {
            return scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e){
            logger.error("add-job-exception:name-" + jobDetail.getKey().getName() +
                    ",group-" + jobDetail.getKey().getGroup());
            logger.error("add-trigger-exception:name-" + trigger.getKey().getName() +
                    ",group-" + trigger.getKey().getGroup());
            logger.error("SchedulerException:", e);
        }
        return null;
    }

    private boolean checkExists(JobKey jobKey){
        try {
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e){
            logger.error("check-job-exception:name-" + jobKey.getName() + ",group-" + jobKey.getGroup());
            logger.error("SchedulerException:", e);
        }
        return false;
    }

    private List<? extends Trigger> getTriggersOfJob(JobKey jobKey){
        try {
            return scheduler.getTriggersOfJob(jobKey);
        } catch (SchedulerException e){
            logger.error("get-triggers-by-job:name-" + jobKey.getName() + ",group-" + jobKey.getGroup());
            logger.error("SchedulerException:", e);
        }
        return null;
    }

    private boolean unScheduleJob(TriggerKey triggerKey){
        try {
            return scheduler.unscheduleJob(triggerKey);
        } catch (SchedulerException e){
            logger.error("un-schedule-job-exception:name-" + triggerKey.getName() + ",group-" + triggerKey.getGroup());
            logger.error("SchedulerException:", e);
        }
        return false;
    }

    private JobKey jobKey(ScheduleJob job){
        if(job == null){
            return null;
        }
        return new JobKey(job.getJobName(),job.getJobGroup());
    }
}
