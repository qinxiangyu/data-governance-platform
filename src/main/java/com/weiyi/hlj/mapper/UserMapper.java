package com.weiyi.hlj.mapper;

import com.weiyi.hlj.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author system
 * @since 2019-06-25
 */
public interface UserMapper extends BaseMapper<User> {

    User selectById(long id);

}
