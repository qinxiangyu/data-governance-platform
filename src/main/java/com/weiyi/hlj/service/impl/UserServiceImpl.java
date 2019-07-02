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
