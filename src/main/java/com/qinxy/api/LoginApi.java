package com.qinxy.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qinxy.common.Constants;
import com.qinxy.service.UserService;
import com.qinxy.common.BaseJsonObject;
import com.qinxy.dto.LoginDTO;
import com.qinxy.entity.User;
import com.qinxy.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qinxy on 2019/7/2.
 */
@RestController
public class LoginApi extends BaseApi {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    @ResponseBody
    public BaseJsonObject<String> login(@RequestBody LoginDTO loginDTO){
        logger.info("login info:{}", JSONObject.toJSONString(loginDTO));
        String username = loginDTO.getUsername();
        String passWord = loginDTO.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(passWord)) {
            return BaseJsonObject.failResp("请输入用户名密码");
        }
        if(!"admin".equals(username) || !"123456".equals(passWord)){
            return BaseJsonObject.failResp("用户名或密码错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        String token = jwtUtil.generateToken(user);
        return BaseJsonObject.successResp(token);
    }

    @GetMapping(value = "/getUserInfo")
    @ResponseBody
    public BaseJsonObject<User> getUserInfo(){
        User user = (User) request.getAttribute(Constants.USER_INFO);
        return BaseJsonObject.successResp(user);
    }
}
