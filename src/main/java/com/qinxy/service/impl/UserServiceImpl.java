package com.qinxy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qinxy.mapper.UserMapper;
import com.qinxy.entity.User;
import com.qinxy.service.UserService;
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
 * @since 2019-07-02
 */
@Service
public class UserServiceImpl extends CommonService<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(int i) {
        return userMapper.selectById(i);
    }

    @Override
    @DS("#dataSource")
    public List<User> dyList(String dataSource) {
        return userMapper.selectList(null);
    }
}
