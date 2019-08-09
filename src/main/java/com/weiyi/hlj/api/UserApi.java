package com.weiyi.hlj.api;


import com.weiyi.hlj.common.BaseJsonObject;
import com.weiyi.hlj.dto.SQLGenerateDTO;
import com.weiyi.hlj.entity.User;
import com.weiyi.hlj.service.RedisService;
import com.weiyi.hlj.service.UserService;
import com.weiyi.hlj.utils.SQLParserUtil;
import com.weiyi.hlj.utils.SelectObject;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.beans.Expression;
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

    @PostMapping(value = "/generateSQL")
    @ResponseBody
    public String generate(@RequestBody SQLGenerateDTO sqlGenerateDTO) throws JSQLParserException {
        PlainSelect plainSelect = new PlainSelect();

        String[] items = sqlGenerateDTO.getSelect().split(",");
        List<SelectItem> list = new ArrayList<>();
        for (String item:items) {
            SelectExpressionItem selectItem = new SelectExpressionItem();
            Column column = new Column();
            column.setColumnName(item);
            selectItem.setExpression(column);
            list.add(selectItem);
        }
        plainSelect.setSelectItems(list);


        Table table = new Table(sqlGenerateDTO.getTable());
        plainSelect.setFromItem(table);

        EqualsTo equalsTo = new EqualsTo();
        Column column = new Column();
        column.setColumnName("name");
        equalsTo.setLeftExpression(column);
        equalsTo.setRightExpression(new StringValue("张三"));

        plainSelect.setWhere(new Parenthesis(equalsTo));

        logger.info("sql:{}",plainSelect);
        return plainSelect.toString();
    }



}
