package com.weiyi.hlj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weiyi.hlj.entity.ScheduleJob;
import com.weiyi.hlj.mapper.ScheduleJobMapper;
import com.weiyi.hlj.service.ScheduleJobService;
import com.weiyi.hlj.service.CommonService;
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
