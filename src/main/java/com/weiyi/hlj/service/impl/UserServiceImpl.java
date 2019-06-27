package com.weiyi.hlj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.weiyi.hlj.entity.User;
import com.weiyi.hlj.mapper.UserMapper;
import com.weiyi.hlj.service.UserService;
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
 * @since 2019-06-25
 */
@Service
@DS("master1")
public class UserServiceImpl extends CommonService<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @param tenantName 请求的数据源
     * @return
     */
    @Override
    @DS("#tenantName")
    public List<User> dyList(String tenantName) {

        logger.info("tenantName :{}",tenantName);
        //获取当前数据源，判断数据源是什么类型,根据类型调用相应的sql进行查询数据库表名，和表字段名
        return userMapper.selectList(null);
    }

    @Override
    public User selectById(int i) {
        return userMapper.selectById(i);
    }


}
