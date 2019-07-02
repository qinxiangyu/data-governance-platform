package com.weiyi.hlj.api;

import com.alibaba.fastjson.JSONObject;
import com.weiyi.hlj.common.BaseJsonObject;
import com.weiyi.hlj.common.Constants;
import com.weiyi.hlj.dto.AccountCredentials;
import com.weiyi.hlj.entity.User;
import com.weiyi.hlj.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qinxy on 2019/7/2.
 */
@RestController
public class LoginApi extends BaseApi {

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping(value = "/login")
    @ResponseBody
    public BaseJsonObject<String> login(AccountCredentials accountCredentials){
        logger.info("login info:{}", JSONObject.toJSONString(accountCredentials));
        String username = accountCredentials.getUsername();
        String passWord = accountCredentials.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(passWord)) {
            return BaseJsonObject.failResp("请输入用户名密码");
        }
        if(!"admin".equals(username) || !"123456".equals(passWord)){
            return BaseJsonObject.failResp("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(accountCredentials);
        return BaseJsonObject.successResp(token);

    }

    @GetMapping(value = "/getUserInfo")
    @ResponseBody
    public BaseJsonObject<User> getUserInfo(){
        AccountCredentials user = (AccountCredentials)request.getAttribute(Constants.USER_INFO);
        User user1 = new User();
        user1.setName(user.getUsername());
        return BaseJsonObject.successResp(user1);
    }
}
