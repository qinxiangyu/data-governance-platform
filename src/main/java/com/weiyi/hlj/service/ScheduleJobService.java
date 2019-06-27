package com.weiyi.hlj.service;

import com.weiyi.hlj.entity.ScheduleJob;
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
