package com.weiyi.hlj.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinxy on 2019/7/9.
 */
public class SimpleSelectDemo {


    public static void main(String[] args) {

        SimpleSelectDemo demo = new SimpleSelectDemo();

        String table = "KB_ZX_CUST_PROD_SUM";

        String[][] condition = {{"MONTH", "12"}, {"VIP_TYPE", "高值商客"}, {"ZX_CUST_CODE", "12"}};

        String[][] outColumn = {{"MONTH"}, {"SUM_TOTAL"}};

        String limit = "122";

        demo.selectObjectDemo(table, null, condition, outColumn, null, limit);

        demo.jsqlDemo(table, null, condition, outColumn, null, limit);

    }


    //SelectObject使用
    SelectObject selectObjectDemo(String table, String aliasName, String[][] condition, String[][] outColumn, String[][] groubyColumn, String limit) {

        if (aliasName == null) {

            aliasName = "T_A";

        }

        SelectObject selectObject = new SelectObject();

        //生成表别名，默认userAs为true
        selectObject.generateTableAlias(table, table, aliasName);

        //设置查询的主体
        selectObject.setFromItem(selectObject.getTableByKey(table));

        //加入查询字段
        if (outColumn != null) {

            for (String[] out : outColumn) {

                switch (out.length) {

                    case 1:

                        selectObject.addSelectItem(aliasName + "." + out[0]);

                        break;

                    case 2:

                        selectObject.addSelectItem(out[1] + "(" + aliasName + "." + out[0] + ")");

                        break;

                    case 4:

                        selectObject.addSelectItem(out[1] + "(" + out[2] + "(" + aliasName + "." + out[0] + " as " + out[3] + "))");

                        break;

                }

            }

        } else {

            selectObject.addSelectItem("(*)");

        }

//加入查询条件

        if (condition != null) {

            for (String[] cond : condition) {

                try {

                    selectObject.addWhereExpression("(" + aliasName + "." + cond[0] + "='" + cond[1] + "')");

                } catch (JSQLParserException e) {

                    e.printStackTrace();

                }

            }

        }

//添加group by

        if (groubyColumn != null) {

            for (String[] group : groubyColumn) {

                selectObject.addGroupByExpression(aliasName + "." + group[0]);

            }

        }

//设置Limit

        if (StringUtils.isNotEmpty(limit)) {

            selectObject.setLimit(limit);

        }

        System.out.println("selectObject:" + selectObject);

        return selectObject;

    }


    //JSqlParser直接调用
    PlainSelect jsqlDemo(String ta, String aliasName, String[][] condition, String[][] outColumn, String[][] groubyColumn, String rowCount) {

        if (aliasName == null) {

            aliasName = "T_A";

        }

        PlainSelect plainSelect = new PlainSelect();


        Table table = new Table(ta);

        Alias alias = new Alias(aliasName);

        table.setAlias(alias);

//设置查询的主体

        plainSelect.setFromItem(table);

//加入查询字段

        if (outColumn != null) {

            SelectExpressionItem selectItem = null;

            Expression selectExpression = null;

            Function func = null;

            List<Expression> funcExpression = null;

            CastExpression castExpression = null;

            ColDataType colDataType = null;

            for (String[] out : outColumn) {

                selectExpression = null;

                selectItem = new SelectExpressionItem();

                switch (out.length) {

                    case 1:

                        selectExpression = new Column(aliasName + "." + out[0]);

                        break;

                    case 2:

                        func = new Function();

                        func.setName(out[1]);

                        funcExpression = new ArrayList<>();

                        funcExpression.add(new Column(aliasName + "." + out[0]));

                        func.setParameters(new ExpressionList(funcExpression));

                        selectExpression = func;

                        break;

                    case 4:

                    //此处以cast 为例

                        func = new Function();

                        func.setName(out[1]);

                        funcExpression = new ArrayList<Expression>();

                        castExpression = new CastExpression();

                        castExpression.setLeftExpression(new Column(aliasName + "." + out[0]));

                        colDataType = new ColDataType();

                        colDataType.setDataType(out[3]);

                        castExpression.setType(colDataType);

                        castExpression.setUseCastKeyword(true);

                        funcExpression.add(castExpression);

                        func.setParameters(new ExpressionList(funcExpression));

                        selectExpression = func;

                        break;

                }

                selectItem.setExpression(selectExpression);

                plainSelect.addSelectItems(selectItem);

            }

        } else {

            plainSelect.addSelectItems(new AllColumns());

        }

        //加入查询条件

        if (condition != null) {

            BinaryExpression whereExpression = null;

            for (String[] cond : condition) {

                whereExpression = new EqualsTo();

                whereExpression.setLeftExpression(new Column(aliasName + "." + cond[0]));

                whereExpression.setRightExpression(new StringValue(cond[1]));

                if (plainSelect.getWhere() == null) {

                    plainSelect.setWhere(new Parenthesis(whereExpression));

                } else {

                    plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), new Parenthesis(whereExpression)));

                }

            }

        }

        //添加group by

        if (groubyColumn != null) {

            for (String[] group : groubyColumn) {

                plainSelect.addGroupByColumnReference(new Column(aliasName + "." + group[0]));

            }

        }

        //设置Limit

        if (StringUtils.isNotEmpty(rowCount)) {

            Limit limit = new Limit();

            limit.setRowCount(new LongValue(rowCount));

            plainSelect.setLimit(limit);

        }

        System.out.println("JSqlDemo:" + plainSelect);

        return plainSelect;

    }

}
