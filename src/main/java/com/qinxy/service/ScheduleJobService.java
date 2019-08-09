package com.qinxy.service;

import com.qinxy.entity.ScheduleJob;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author system
 * @since 2019-06-27
 */
public interface ScheduleJobService extends IService<ScheduleJob> {

    List<ScheduleJob> enableList();
}
