package com.weiyi.hlj.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiyi.hlj.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qinxy on 2019/6/24.
 */
public class CommonService<M extends BaseMapper<T>,T extends BaseEntity> extends ServiceImpl<M,T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
