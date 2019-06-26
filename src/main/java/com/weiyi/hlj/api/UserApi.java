package com.weiyi.hlj.api;


import com.weiyi.hlj.common.BaseJsonObject;
import com.weiyi.hlj.entity.User;
import com.weiyi.hlj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by qinxy on 2019/6/21.
 */
@RestController
@RequestMapping(value = "/user")
public class UserApi extends BaseApi{


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public BaseJsonObject<List<User>> test(){
        List<User> userList = userService.list();
        return BaseJsonObject.successResp(userList);
    }

    @GetMapping(value = "/dy")
    @ResponseBody
    public BaseJsonObject<List<User>> dy(@RequestParam(value = "dataSource")String dataSource){
        List<User> userList = userService.dyList(dataSource);
        return BaseJsonObject.successResp(userList);
    }

}
