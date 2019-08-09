package com.qinxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qinxy.entity.ScheduleJob;
import com.qinxy.mapper.ScheduleJobMapper;
import com.qinxy.service.ScheduleJobService;
import com.qinxy.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author system
 * @since 2019-06-27
 */
@Service
public class ScheduleJobServiceImpl extends CommonService<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    @Autowired
    private ScheduleJobMapper scheduleJobMapper;
    @Override
    public List<ScheduleJob> enableList() {
        QueryWrapper<ScheduleJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_enable",true);
        return scheduleJobMapper.selectList(queryWrapper);
    }
}
