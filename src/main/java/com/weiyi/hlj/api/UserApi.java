package com.weiyi.hlj.api;


import com.weiyi.hlj.common.BaseJsonObject;
import com.weiyi.hlj.entity.User;
import com.weiyi.hlj.service.RedisService;
import com.weiyi.hlj.service.UserService;
import com.weiyi.hlj.utils.SQLParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by qinxy on 2019/6/21.
 */
@RestController
@RequestMapping(value = "/user")
public class UserApi extends BaseApi {


    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public BaseJsonObject<User> test() {
        User user = userService.selectById(1);
        return BaseJsonObject.successResp(user);
    }

    @GetMapping(value = "/dy")
    @ResponseBody
    public BaseJsonObject<List<User>> dy(@RequestParam(value = "dataSource") String dataSource) {
        List<User> userList = userService.dyList(dataSource);
        return BaseJsonObject.successResp(userList);
    }

    @GetMapping(value = "/redis")
    @ResponseBody
    public String redis(@RequestParam(value = "key") String key) {
        redisService.redis(key, "123");
        String value = redisService.redis(key);
        return value;
    }

    @PostMapping(value = "/parseSQL")
    @ResponseBody
    public Map<String, Object> sql(@RequestBody String sql) {
        SQLParserUtil util = new SQLParserUtil();
        Map<String, Object> result = util.parseSelectBody(util.getSelectBody(sql));
        return result;
    }



}
