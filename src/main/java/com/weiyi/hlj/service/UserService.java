package com.weiyi.hlj.service;

import com.weiyi.hlj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author system
 * @since 2019-07-02
 */
public interface UserService extends IService<User> {

    User selectById(int i);

    List<User> dyList(String dataSource);
}
