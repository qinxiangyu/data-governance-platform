package com.weiyi.hlj.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by qinxy on 2019/7/11.
 */
@Component
public class SQLParserUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取查询条件
     *
     * @return
     */
    public List<SelectItem> getColumns(SelectBody selectBody) {
        if (selectBody != null) {
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                return plainSelect.getSelectItems();
            }
        }
        return null;
    }

    public List<Expression> getCondition(SelectBody selectBody) {
        List<Expression> list = new ArrayList<>();
        if (selectBody != null) {
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                Expression expression = plainSelect.getWhere();
                if (expression instanceof AndExpression) {
                    AndExpression andExpression = (AndExpression) expression;
                    whereExpression(andExpression, list);
                }
                return list;
            }
        }
        return null;
    }


    /**
     * 获取表名及关联关系
     *
     * @return
     */
    public Map<String, List<Map<String, Object>>> getTableNames(SelectBody selectBody) {
        if (selectBody != null) {
            Map<String, List<Map<String, Object>>> result = new HashMap<>();
            List<Map<String, Object>> tableList = new ArrayList<>();
            List<Map<String, Object>> relationList = new ArrayList<>();
            result.put("tables", tableList);
            result.put("conditions", relationList);
            getTableAndAlias(selectBody, result);
            return result;
        }
        return null;
    }

    public SelectBody getSelectBody(String sql) {
        //获取数据库的操作
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            logger.error("SQL不正确:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        //是查询语句
        if (statement instanceof Select) {
            Select select = (Select) statement;
            //获取查询体
            SelectBody selectBody = select.getSelectBody();
            return selectBody;
        }
        return null;
    }

    private Map<String, List<Map<String, Object>>> getTableAndAlias(SelectBody selectBody, Map<String, List<Map<String, Object>>> result) {
        //普通查询语句
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            //获取from元素
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem instanceof Table) {
                Table table = (Table) fromItem;
                Map<String, Object> map = new HashMap<>();
                result.get("tables").add(map);
                map.put(table.getAlias().getName(), table.getName());
            } else if (fromItem instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) fromItem;
                SelectBody subSelectBody = subSelect.getSelectBody();
                getTableAndAlias(subSelectBody, result);
            }
            //获取join关系
            List<Join> joins = plainSelect.getJoins();
            if (joins != null) {
                for (Join join : joins) {
                    String relation = "unknown";
                    Map<String, Object> map = new HashMap<>();
                    result.get("tables").add(map);
                    if (join.isLeft()) {
                        relation = JoinEnum.LEFT.toString();
                    } else if (join.isInner()) {
                        relation = JoinEnum.INNER.toString();
                    } else if (join.isOuter()) {
                        relation = JoinEnum.OUTER.toString();
                    } else if (join.isRight()) {
                        relation = JoinEnum.RIGHT.toString();
                    } else if (join.isNatural()) {
                        relation = JoinEnum.NATURE.toString();
                    } else if (join.isFull()) {
                        relation = JoinEnum.FULL.toString();
                    } else if (join.isSimple()) {
                        relation = JoinEnum.SIMPLE.toString();
                    }
                    map.put("relation", relation);
                    Expression onExpression = join.getOnExpression();
                    FromItem rightItem = join.getRightItem();
                    //如果是表
                    if (rightItem instanceof Table) {//first primary table
                        Table table = (Table) rightItem;
                        map.put(table.getAlias().getName(), table.getName());
                    } else if (rightItem instanceof SubSelect) {
                        SubSelect subSelect = (SubSelect) rightItem;
                        map.put(subSelect.getAlias().getName(),subSelect.getSelectBody());
                    }
                    joinExpression(onExpression, result.get("conditions"));
                }
            }

        } else if (selectBody instanceof SetOperationList) {//几何操作
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selects = setOperationList.getSelects();
            for (SelectBody selectBody3 : selects) {
                getTableAndAlias(selectBody3, result);
            }
        }

        return result;
    }

    private void joinExpression(Expression expression, List<Map<String, Object>> expressionMap) {
        if (expression instanceof EqualsTo) {
            EqualsTo equalsTo = (EqualsTo) expression;
            Expression rightExpression = equalsTo.getRightExpression();
            Expression leftExpression = equalsTo.getLeftExpression();
            if (rightExpression instanceof Column && leftExpression instanceof Column) {
                Column rightColumn = (Column) rightExpression;
                Column leftColumn = (Column) leftExpression;
                Map<String, Object> map = new HashMap<>();
                map.put(leftColumn.toString(), rightColumn.toString());
                expressionMap.add(map);
            }
        } else if (expression instanceof AndExpression) {
            AndExpression andExpression = (AndExpression) expression;
            Expression leftExpression = andExpression.getLeftExpression();
            joinExpression(leftExpression, expressionMap);
            Expression rightExpression = andExpression.getRightExpression();
            joinExpression(rightExpression, expressionMap);
        }
    }

    public void parseSelectExpression(SelectItem selectItem, Map<String, Object> map) {

        if (selectItem instanceof SelectExpressionItem) {
            SelectExpressionItem item = (SelectExpressionItem) selectItem;
//            logger.info("alias:{}",item.getAlias().getName());
//            logger.info("expression:{}",item.getExpression());
            Expression expression = item.getExpression();
            //函数、caseWhen、字查询
            if (expression instanceof Function || expression instanceof CaseExpression) {
                Map<String, Map<Object, Object>> function = parseFunction(expression);
                map.put(item.getAlias().getName(), function);
            } else {
                //普通查询
                map.put(item.getAlias().getName(), item.getExpression());
            }
        } else {
            logger.info("表达式不规范！selectItem:{}", selectItem);
        }
    }

    public Map<String, Map<Object, Object>> parseFunction(Expression expression) {
        Map<String, Map<Object, Object>> exprMap = new HashMap<>();
        Map<Object, Object> functions = new HashMap<>();
        Map<Object, Object> cases = new HashMap<>();
        if (expression instanceof Function) {
            Function function = (Function) expression;
//            logger.info("function name:{}",function.getName());
//            logger.info("function parameters:{}",function.getParameters());
            functions.put(function.getName(), function.getParameters());
            exprMap.put("function", functions);
        } else if (expression instanceof CaseExpression) {
            CaseExpression caseExpression = (CaseExpression) expression;
            List<WhenClause> whenClauses = caseExpression.getWhenClauses();
            Map<Object, Object> results = new HashMap<>();
            whenClauses.forEach(clause -> {
                results.put(clause.getWhenExpression(), clause.getThenExpression());
            });
//            results.put("else", caseExpression.getElseExpression());
            Expression switchCase = caseExpression.getSwitchExpression();
//            logger.info("when then:{}",results);
            if (caseExpression.getSwitchExpression() == null) {
                Expression whenExpression = whenClauses.get(0).getWhenExpression();
                if (whenExpression instanceof ComparisonOperator || whenExpression instanceof IsNullExpression) {
                    ComparisonOperator operator = (ComparisonOperator) whenExpression;
                    switchCase = operator.getLeftExpression();
                    cases.put(switchCase.toString(), results);
                } else if (whenExpression instanceof SubSelect) {//如果case是子查询,子查询走一遍
                    SubSelect subSelect = new SubSelect();
                    Map<String, Object> subResult = parseSelectBody(subSelect.getSelectBody());
                    Map<String, Map<String, Object>> res = new HashMap<>();
                    res.put("subSelect", subResult);
                    cases.put(res, results);
                }
            } else {
                cases.put(switchCase.toString(), results);
            }
//            logger.info("switch:{}", switchCase);
            exprMap.put("case", cases);

        } else {
            logger.info("不支持的表达式:{}",expression.getClass().getSimpleName());

        }
        return exprMap;
    }

    public Map<String, Object> parseSelectBody(SelectBody selectBody) {
        Map<String, Object> result = new HashMap<>();
        Map<String, List<Map<String, Object>>> tableNames = getTableNames(selectBody);
        logger.info("tableNames:{}", tableNames);
        Map<String, Object> conditionMap = new HashMap<>();
        getCondition(selectBody).forEach(condition -> {
            parseWhereExpression(condition, conditionMap);
        });
        logger.info("conditionMap:{}", conditionMap);
        Map<String, Object> columnMap = new HashMap<>();
        getColumns(selectBody).forEach(item -> {
            parseSelectExpression(item, columnMap);
        });
        logger.info("columnMap:{}", columnMap);
        result.put("tables", tableNames);
        result.put("conditions", conditionMap);
        result.put("columns", columnMap);
        return result;

    }

    public void parseWhereExpression(Expression expression, Map<String, Object> map) {
        Map<Expression, Expression> equalsMap = new HashMap<>();
        map.put("equalsTo", equalsMap);
        Map<Expression, Expression> notEqualsMap = new HashMap<>();
        map.put("notEqualsTo", notEqualsMap);
        Map<Expression, Expression> gtMap = new HashMap<>();
        map.put("greaterThan", gtMap);
        Map<Expression, Expression> gteMap = new HashMap<>();
        map.put("greaterThanEquals", gteMap);
        Map<Expression, Expression> ltMap = new HashMap<>();
        map.put("lessThan", ltMap);
        Map<Expression, Expression> lteMap = new HashMap<>();
        map.put("lessThanEquals", lteMap);
        Map<Expression, ItemsList> inMap = new HashMap<>();
        map.put("in", inMap);
        Map<Expression, ItemsList> notInMap = new HashMap<>();
        map.put("notIn", notInMap);
        List<Expression> nullList = new ArrayList<>();
        map.put("isNull", nullList);
        List<Expression> notNullList = new ArrayList<>();
        map.put("notNull", notNullList);
        Map<Expression, Expression> likeMap = new HashMap<>();
        map.put("like", likeMap);
        Map<Expression, Map<Object, Expression>> caseMap = new HashMap<>();
        map.put("case", caseMap);
        Map<Expression, Map<String, Expression>> betweenMap = new HashMap<>();
        map.put("between", betweenMap);
        List<Object> subList = new ArrayList<>();
        map.put("subList", subList);
        List<Object> functionList = new ArrayList<>();
        map.put("functionList", functionList);
        List<Expression> existList = new ArrayList<>();
        map.put("exist", existList);
        List<Expression> notExistList = new ArrayList<>();
        map.put("notExist", notExistList);
        if (expression instanceof EqualsTo) {
            EqualsTo equalsTo = (EqualsTo) expression;
            equalsMap.put(equalsTo.getLeftExpression(), equalsTo.getRightExpression());
        } else if (expression instanceof NotEqualsTo) {
            NotEqualsTo notEqualsTo = (NotEqualsTo) expression;
            equalsMap.put(notEqualsTo.getLeftExpression(), notEqualsTo.getRightExpression());
        } else if (expression instanceof GreaterThan) {
            GreaterThan greaterThan = (GreaterThan) expression;
            gtMap.put(greaterThan.getLeftExpression(), greaterThan.getRightExpression());
        } else if (expression instanceof GreaterThanEquals) {
            GreaterThanEquals greaterThanEquals = (GreaterThanEquals) expression;
            gteMap.put(greaterThanEquals.getLeftExpression(), greaterThanEquals.getRightExpression());
        } else if (expression instanceof InExpression) {
            InExpression in = (InExpression) expression;
            if (in.isNot())
                notInMap.put(in.getLeftExpression(), in.getRightItemsList());
            else
                inMap.put(in.getLeftExpression(), in.getRightItemsList());
        } else if (expression instanceof MinorThan) {
            MinorThan minorThan = (MinorThan) expression;
            ltMap.put(minorThan.getLeftExpression(), minorThan.getRightExpression());

        } else if (expression instanceof MinorThanEquals) {
            MinorThanEquals minorThanEquals = (MinorThanEquals) expression;
            lteMap.put(minorThanEquals.getLeftExpression(), minorThanEquals.getRightExpression());
        } else if (expression instanceof LikeExpression) {
            LikeExpression likeExpression = (LikeExpression) expression;
            likeMap.put(likeExpression.getLeftExpression(), likeExpression.getRightExpression());
        } else if (expression instanceof IsNullExpression) {
            IsNullExpression isNullExpression = (IsNullExpression) expression;
            if (isNullExpression.isNot())
                notNullList.add(isNullExpression.getLeftExpression());
            else
                nullList.add(isNullExpression.getLeftExpression());
        } else if (expression instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) expression;
//            subList.add(subSelect.getSelectBody());
            subList.add(parseSelectBody(subSelect.getSelectBody()));
        } else if (expression instanceof Between) {
            Between between = (Between) expression;
            Map<String, Expression> expressionMap = new HashMap<>();
            expressionMap.put("start", between.getBetweenExpressionStart());
            expressionMap.put("end", between.getBetweenExpressionEnd());
            betweenMap.put(between.getLeftExpression(), expressionMap);
        } else if (expression instanceof CaseExpression) {
            CaseExpression caseExpression = (CaseExpression) expression;
            List<WhenClause> whenClauses = caseExpression.getWhenClauses();
            Map<Object, Expression> results = new HashMap<>();
            whenClauses.forEach(clause -> {
                results.put(clause.getWhenExpression(), clause.getThenExpression());
            });
            results.put("else", caseExpression.getElseExpression());
            caseMap.put(caseExpression.getSwitchExpression(), results);
        } else if (expression instanceof Function) {
//            Function function = (Function) expression;
//            logger.info("function name:{}", function.getName());
//            logger.info("function parameters:{}", function.getParameters());
            Map<String, Map<Object, Object>> function = parseFunction(expression);
            functionList.add(function);
        } else if (expression instanceof ExistsExpression) {
            ExistsExpression existsExpression = (ExistsExpression) expression;
            if (existsExpression.isNot())
                notExistList.add(existsExpression.getRightExpression());
            else
                existList.add(existsExpression.getRightExpression());
        } else {
            logger.error("不支持的表达式");
        }
    }

    private void whereExpression(Expression expression, List<Expression> expressions) {
        if (expression instanceof AndExpression) {
            Expression left = ((AndExpression) expression).getLeftExpression();
            expressions.add(left);
            Expression right = ((AndExpression) expression).getRightExpression();
            whereExpression(right, expressions);
        } else if (expression instanceof OrExpression) {
            Expression left = ((OrExpression) expression).getLeftExpression();
            expressions.add(left);
            Expression right = ((OrExpression) expression).getRightExpression();
            whereExpression(right, expressions);
        } else {
            //最后一个条件
            expressions.add(expression);
            return;
        }
    }


}
