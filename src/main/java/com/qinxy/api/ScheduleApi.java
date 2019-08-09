package com.qinxy.api;

import com.qinxy.plugins.SchedulerKit;
import com.qinxy.entity.ScheduleJob;
import com.qinxy.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qinxy on 2019/6/27.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleApi extends BaseApi {

    @Autowired
    private SchedulerKit schedulerKit;

    @Autowired
    private ScheduleJobService scheduleJobService;

    @GetMapping(value = "/switch")
    public boolean switchJob(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "enable") Boolean enable) {
        ScheduleJob job = new ScheduleJob();
        job.setId(id);
        job.setJobEnable(enable);
        scheduleJobService.updateById(job);
        job = scheduleJobService.getById(id);
        if (job == null) {
            return false;
        }
        if (enable) {
            schedulerKit.scheduleJob(job);
            logger.info("schedule plugin starting...");

        } else {
            schedulerKit.cancelJob(job);
            logger.info("schedule plugin stopping...");

        }

        return true;


    }
}
