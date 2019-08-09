package com.weiyi.hlj.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinxy on 2019/7/9.
 */
public class SelectObject {

    private PlainSelect plainSelect;
    private Map<String, Table> mapJPTable;
    private SetOperationList unionSelect;

    public SelectObject() {
        super();
        setPlainSelect(new PlainSelect());
    }

    public SelectObject(PlainSelect plainSelect) {
        super();
        setPlainSelect(plainSelect);
    }

    /**
     * 生成表别名
     *
     * @param key
     * @param tableName
     * @param aliasName
     * @param useAs     默认为true
     */
    public void generateTableAlias(String key, String tableName, String aliasName, boolean useAs) {
        Table jpTable = new Table(tableName);
        jpTable.setAlias(new Alias(aliasName, useAs));
        if (mapJPTable == null) {
            mapJPTable = new HashMap<>();
        }
        mapJPTable.put(key, jpTable);
    }

    /**
     * 生成表别名
     *
     * @param key
     * @param tableName
     * @param aliasName useAs 默认为true
     */
    public void generateTableAlias(String key, String tableName, String aliasName) {
        generateTableAlias(key, tableName, aliasName, true);
    }

    /**
     * 根据key获取Table
     *
     * @param key
     * @return Table
     */
    public Table getTableByKey(String key) {
        if (mapJPTable == null) {
            return null;
        }
        return mapJPTable.get(key);
    }

    /**
     * 根据key获取别名
     *
     * @param key
     * @return String
     */
    public String getTableAlias(String key) {
        Table table = getTableByKey(key);
        if (table != null && table.getAlias() != null) {
            return table.getAlias().getName();
        }
        return null;
    }

    /**
     * 添加查询体
     *
     * @param table
     * @param columnName
     * @param aliasName  useAs 默认为true
     */
    public void addSelectItem(Table table, String columnName, String aliasName) {
        addSelectItem(table, columnName, aliasName, true);
    }

    /**
     * 添加查询体
     *
     * @param table
     * @param columnName
     * @param aliasName
     * @param useAs      默认为true
     */
    public void addSelectItem(Table table, String columnName, String aliasName, boolean useAs) {
        SelectExpressionItem selectItem = new SelectExpressionItem();
        selectItem.setExpression(new Column(table, columnName));
        if (StringUtils.isNotEmpty(aliasName)) {
            selectItem.setAlias(new Alias(aliasName, useAs));
        }
        addSelectItem(selectItem);
    }

    /**
     * 添加查询体
     *
     * @param selectItem
     */
    public void addSelectItem(SelectItem selectItem) {
        if (plainSelect.getSelectItems() == null) {
            plainSelect.addSelectItems(selectItem);
            return;
        }
        if (!contains(plainSelect.getSelectItems(), selectItem)) {
            plainSelect.addSelectItems(selectItem);
        }
    }

    /**
     * 添加查询体 该方法不会生成别名
     *
     * @param selectString
     */
    public void addSelectItem(String selectString) {
        SelectExpressionItem selectItem = new SelectExpressionItem();
        try {
            selectItem.setExpression(CCJSqlParserUtil.parseCondExpression(selectString));
            addSelectItem(selectItem);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加查询体
     *
     * @param selectString
     * @param aliasName
     * @param useAs        默认为true
     */
    public void addSelectItem(String selectString, String aliasName, boolean useAs) {
        SelectExpressionItem selectItem = new SelectExpressionItem();
        try {
            selectItem.setExpression(CCJSqlParserUtil.parseCondExpression(selectString));
            if (StringUtils.isNotEmpty(aliasName)) {
                selectItem.setAlias(new Alias(aliasName, useAs));
            }
            addSelectItem(selectItem);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加查询体
     *
     * @param selectString useAs 默认为true
     */
    public void addSelectItem(String selectString, String aliasName) {
        addSelectItem(selectString, aliasName, true);
    }

    /**
     * 设置查询表
     *
     * @param item
     */
    public void setFromItem(FromItem item) {
        plainSelect.setFromItem(item);
    }

    /**
     * 添加join左外连接
     *
     * @return
     */
    public Join addJoinLO(FromItem rightItem) {
        return addJoin(rightItem, true, false, true, false);
    }

    /**
     * 添加join左内连接
     *
     * @return
     */
    public Join addJoinLI(FromItem rightItem) {
        return addJoin(rightItem, true, true, false, false);
    }

    /**
     * 添加join连接
     *
     * @return
     */
    public Join addJoin(FromItem rightItem) {
        return addJoin(rightItem, false, false, false, false);
    }

    /**
     * 添加join连接
     *
     * @param join
     * @return Join
     */
    public Join addJoin(Join join) {
        return addJoin(join, false, false, false, false);
    }

    /**
     * 添加join连接
     *
     * @param tableName
     * @return
     */
    public Join addJoin(String tableName) {
        return addJoin(tableName, null, false);
    }

    /**
     * 添加join连接
     *
     * @param tableName
     * @param aliasName
     * @param useAs
     * @return
     */
    public Join addJoin(String tableName, String aliasName, boolean useAs) {
        Table table = new Table(tableName);
        if (StringUtils.isNotEmpty(aliasName)) {
            table.setAlias(new Alias(aliasName, useAs));
        }
        return addJoin(table, false, false, false, false);
    }

    /**
     * 添加join连接
     *
     * @param rightItem
     * @param left
     * @param inner
     * @param outter
     * @param right
     * @return Join
     */
    public Join addJoin(FromItem rightItem, boolean left, boolean inner, boolean outter, boolean right) {
        Join join = new Join();
        join.setRightItem(rightItem);
        return addJoin(join, left, inner, outter, right);
    }

    /**
     * 添加join连接
     *
     * @param join
     * @param left
     * @param inner
     * @param outter
     * @param right
     * @return
     */
    public Join addJoin(Join join, boolean left, boolean inner, boolean outter, boolean right) {
        if (left) {
            join.setLeft(left);
        } else if (right) {
            join.setRight(right);
        }
        if (inner) {
            join.setInner(inner);
        } else if (outter) {
            join.setOuter(outter);
        }
        if (!contains(getListJoin(), join)) {
            getListJoin().add(join);
        }
        return join;
    }

    /**
     * 添加join连接条件
     *
     * @param join
     * @param onExpression
     * @param isOr
     */
    public static void addJoinOnExpression(Join join, Expression onExpression, boolean isOr) {
        if (join == null) {
            return;
        }
        if (join.getOnExpression() == null) {
            join.setOnExpression(onExpression);
            return;
        }
        if (isOr) {
            join.setOnExpression(new OrExpression(join.getOnExpression(), onExpression));
        } else {
            join.setOnExpression(new AndExpression(join.getOnExpression(), onExpression));
        }
    }

    /**
     * 添加join连接条件
     *
     * @param join
     * @param onExpression 默认为 and连接
     */
    public static void addJoinOnExpression(Join join, Expression onExpression) {
        addJoinOnExpression(join, onExpression, false);
    }

    /**
     * 添加join连接条件
     *
     * @param join
     * @param onExpression
     * @throws JSQLParserException 默认为 and连接
     */
    public static void addJoinOnExpression(Join join, String onExpression) throws JSQLParserException {
        addJoinOnExpression(join, CCJSqlParserUtil.parseCondExpression(onExpression));
    }

    /**
     * 添加join连接条件
     *
     * @param join
     * @param onExpression
     * @param isOr
     * @throws JSQLParserException
     */
    public static void addJoinOnExpression(Join join, String onExpression, boolean isOr) throws JSQLParserException {
        addJoinOnExpression(join, CCJSqlParserUtil.parseCondExpression(onExpression), isOr);
    }

    /**
     * 添加where条件
     *
     * @param whereExpression
     * @param isOr
     */
    public void addWhereExpression(Expression whereExpression, boolean isOr) {
        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(whereExpression);
            return;
        }
        if (isOr) {
            plainSelect.setWhere(new OrExpression(plainSelect.getWhere(), whereExpression));
        } else {
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), whereExpression));
        }
    }

    /**
     * 添加where条件
     *
     * @param whereExpression 默认为 and连接
     */
    public void addWhereExpression(Expression whereExpression) {
        addWhereExpression(whereExpression, false);
    }

    /**
     * 添加where条件
     *
     * @param whereExpression
     * @throws JSQLParserException 默认为 and连接
     */
    public void addWhereExpression(String whereExpression) throws JSQLParserException {
        addWhereExpression(CCJSqlParserUtil.parseCondExpression(whereExpression));
    }

    /**
     * 添加where条件
     *
     * @param whereExpression
     * @param isOr
     * @throws JSQLParserException
     */
    public void addWhereExpression(String whereExpression, boolean isOr) throws JSQLParserException {
        addWhereExpression(CCJSqlParserUtil.parseCondExpression(whereExpression), isOr);
    }

    /**
     * 添加groupBy语句
     *
     * @param groupByString
     */
    public void addGroupByExpression(String groupByString) {
        try {
            Expression groupByExpression = CCJSqlParserUtil.parseCondExpression(groupByString);
            addGroupByExpression(groupByExpression);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加groupBy语句
     *
     * @param groupByExpression
     */
    public void addGroupByExpression(Expression groupByExpression) {
        GroupByElement byElement = plainSelect.getGroupBy();
        List<Expression> listGroupByExpression = byElement.getGroupByExpressions();
        if (listGroupByExpression == null) {
            plainSelect.addGroupByColumnReference(groupByExpression);
            return;
        }
        if (!contains(listGroupByExpression, groupByExpression)) {
            //如果为空说明有问题
            plainSelect.addGroupByColumnReference(groupByExpression);
        }
    }

    /**
     * 添加having语句
     *
     * @param havingExpression
     * @param isOr
     */
    public void addHavingExpression(Expression havingExpression, boolean isOr) {
        if (plainSelect.getHaving() == null) {
            plainSelect.setHaving(havingExpression);
            return;
        }
        if (isOr) {
            plainSelect.setHaving(new OrExpression(plainSelect.getHaving(), havingExpression));
        } else {
            plainSelect.setHaving(new AndExpression(plainSelect.getHaving(), havingExpression));
        }
    }

    /**
     * 添加having语句
     *
     * @param havingExpression
     */
    public void addHavingExpression(Expression havingExpression) {
        addWhereExpression(havingExpression, false);
    }

    /**
     * 添加having语句
     *
     * @param havingExpression
     * @throws JSQLParserException
     */
    public void addHavingExpression(String havingExpression) throws JSQLParserException {
        addWhereExpression(CCJSqlParserUtil.parseCondExpression(havingExpression));
    }

    /**
     * 添加having语句
     *
     * @param havingExpression
     * @param isOr
     * @throws JSQLParserException
     */
    public void addHavingExpression(String havingExpression, boolean isOr) throws JSQLParserException {
        addWhereExpression(CCJSqlParserUtil.parseCondExpression(havingExpression), isOr);
    }


    /**
     * 添加orderBy语句
     *
     * @param orderByElement
     * @return
     */
    public boolean addOrderByExpression(OrderByElement orderByElement) {
        boolean result = false;
        if (!contains(getOrderByElements(), orderByElement)) {
            //如果为空说明有问题
            result |= getOrderByElements().add(orderByElement);
        }
        return result;
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @param isNullsFirst
     * @return
     */
    public boolean addOrderByExpression(Expression orderByExpression, boolean isDesc, boolean isNullsFirst) {
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(orderByExpression);
        if (isDesc) {
            orderByElement.setAsc(!isDesc);
        } else {
            orderByElement.setAscDescPresent(!isDesc);
        }
        if (isNullsFirst) {
            orderByElement.setNullOrdering(OrderByElement.NullOrdering.NULLS_FIRST);
        } else {
            orderByElement.setNullOrdering(OrderByElement.NullOrdering.NULLS_LAST);
        }
        return addOrderByExpression(orderByElement);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @return
     */
    public boolean addOrderByExpression(Expression orderByExpression, boolean isDesc) {
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(orderByExpression);
        if (isDesc) {
            orderByElement.setAsc(!isDesc);
        } else {
            orderByElement.setAscDescPresent(!isDesc);
        }
        return addOrderByExpression(orderByElement);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @return
     */
    public boolean addOrderByExpression(Expression orderByExpression) {
        return addOrderByExpression(orderByExpression, false);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @param isNullsFirst
     * @return
     * @throws JSQLParserException
     */
    public boolean addOrderByExpression(String orderByExpression, boolean isDesc, boolean isNullsFirst) throws JSQLParserException {
        return addOrderByExpression(CCJSqlParserUtil.parseCondExpression(orderByExpression), isDesc, isNullsFirst);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @return
     * @throws JSQLParserException
     */
    public boolean addOrderByExpression(String orderByExpression, boolean isDesc) throws JSQLParserException {
        return addOrderByExpression(CCJSqlParserUtil.parseCondExpression(orderByExpression), isDesc);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @return
     * @throws JSQLParserException
     */
    public boolean addOrderByExpression(String orderByExpression) throws JSQLParserException {
        return addOrderByExpression(orderByExpression, false);
    }

    private void setSubselectBody(SelectBody selectBody, String aliasName, boolean userAs) {
        SubSelect subSelect = new SubSelect();
        subSelect.setSelectBody(selectBody);
        if (StringUtils.isNotEmpty(aliasName)) {
            subSelect.setAlias(new Alias(aliasName, userAs));
        }
        setFromItem(subSelect);
    }

    /**
     * 添加子查询
     *
     * @param selectObject
     * @param aliasName
     * @param userAs
     */
    public void setSubselectObject(SelectObject selectObject, String aliasName, boolean userAs) {
        if (selectObject != null) {
            setSubselectBody(selectObject.getSelectBody(), aliasName, userAs);
        }
    }

    /**
     * 添加子查询
     *
     * @param selectObject
     */
    public void setSubselectObject(SelectObject selectObject) {
        setSubselectObject(selectObject, null, false);
    }

    /**
     * 设置Limit
     *
     * @param rowCount
     */
    public void setLimit(String rowCount) {
        Limit limit = new Limit();
        limit.setRowCount(new LongValue(rowCount));
        plainSelect.setLimit(limit);
    }

    /**
     * 获取Limit
     */
    public Limit getLimit() {
        return plainSelect.getLimit();
    }

    /**
     * 设置Offset
     *
     * @param start
     */
    public void setOffset(long start) {
        Offset offset = new Offset();
        offset.setOffset(start);
        plainSelect.setOffset(offset);
    }

    /**
     * 获取Offset
     */
    public Offset getOffset() {
        return plainSelect.getOffset();
    }

    /**
     * 添加unionSelect
     *
     * @param selectObject
     * @param isBracket    默认为false
     * @param isUnionAll   默认为false
     */
    public void addUnionSelect(SelectObject selectObject, boolean isBracket, boolean isUnionAll) {
        if (selectObject != null) {
            addUnionSelectBody(selectObject.getSelectBody(), isBracket, isUnionAll);
        }
    }

    /**
     * 添加unionSelect
     *
     * @param selectObject
     * @param isBracket    isUnionAll 默认为false
     */
    public void addUnionSelect(SelectObject selectObject, boolean isBracket) {
        addUnionSelect(selectObject, isBracket, false);
    }

    /**
     * 添加unionSelect
     *
     * @param selectObject
     */
    public void addUnionSelect(SelectObject selectObject) {
        addUnionSelect(selectObject, false, false);
    }

    /**
     * 添加unionSelect
     *
     * @param plainSelect
     * @param isBracket
     * @param isUnionAll
     */
    public void addUnionSelect(PlainSelect plainSelect, boolean isBracket, boolean isUnionAll) {
        if (plainSelect != null) {
            addUnionSelectBody(plainSelect, isBracket, isUnionAll);
        }
    }

    /**
     * 添加unionSelect
     *
     * @param plainSelect
     * @param isBracket
     */
    public void addUnionSelect(PlainSelect plainSelect, boolean isBracket) {
        addUnionSelect(plainSelect, isBracket, false);
    }

    /**
     * 添加unionSelect
     *
     * @param plainSelect
     */
    public void addUnionSelect(PlainSelect plainSelect) {
        addUnionSelect(plainSelect, false, false);
    }

    private void addUnionSelectBody(SelectBody selectBody, boolean isBracket, boolean isUnionAll) {
        if (selectBody != null) {
            if (unionSelect == null) {
                unionSelect = new SetOperationList();
            }
            List<SelectBody> selects = unionSelect.getSelects();
            List<Boolean> brackets = unionSelect.getBrackets();
            if (selects == null) {
                selects = new ArrayList<>();
            }
            if (brackets == null) {
                brackets = new ArrayList<Boolean>();
            }
            List<SetOperation> operations = unionSelect.getOperations();
            if (operations == null) {
                operations = new ArrayList<SetOperation>();
            }
            //本身作为第一条语句
            if (selects.size() == 0) {
                selects.add(getSelectBody());
                brackets.add(false);
            }

            selects.add(selectBody);
            brackets.add(isBracket);
            UnionOp operation = new UnionOp();
            if (isUnionAll) {
                operation.setAll(isUnionAll);
            }
            operations.add(operation);
            unionSelect.setBracketsOpsAndSelects(brackets, selects, operations);
        }
    }


    /**
     * 添加orderBy语句
     *
     * @param orderByElement
     * @return
     */
    public boolean addUnionOrderByExpression(OrderByElement orderByElement) {
        boolean result = false;
        if (!contains(getUnionOrderByElements(), orderByElement)) {
            //如果为空说明有问题
            result |= getUnionOrderByElements().add(orderByElement);
        }
        return result;
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @param isNullsFirst
     * @return
     */
    public boolean addUnionOrderByExpression(Expression orderByExpression, boolean isDesc, boolean isNullsFirst) {
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(orderByExpression);
        if (isDesc) {
            orderByElement.setAsc(!isDesc);
        } else {
            orderByElement.setAscDescPresent(!isDesc);
        }
        if (isNullsFirst) {
            orderByElement.setNullOrdering(OrderByElement.NullOrdering.NULLS_FIRST);
        } else {
            orderByElement.setNullOrdering(OrderByElement.NullOrdering.NULLS_LAST);
        }
        return addUnionOrderByExpression(orderByElement);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @return
     */
    public boolean addUnionOrderByExpression(Expression orderByExpression, boolean isDesc) {
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(orderByExpression);
        if (isDesc) {
            orderByElement.setAsc(!isDesc);
        } else {
            orderByElement.setAscDescPresent(!isDesc);
        }
        return addUnionOrderByExpression(orderByElement);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @return
     */
    public boolean addUnionOrderByExpression(Expression orderByExpression) {
        return addUnionOrderByExpression(orderByExpression, false);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @param isNullsFirst
     * @return
     * @throws JSQLParserException
     */
    public boolean addUnionOrderByExpression(String orderByExpression, boolean isDesc, boolean isNullsFirst) throws JSQLParserException {
        return addUnionOrderByExpression(CCJSqlParserUtil.parseCondExpression(orderByExpression), isDesc, isNullsFirst);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @param isDesc
     * @return
     * @throws JSQLParserException
     */
    public boolean addUnionOrderByExpression(String orderByExpression, boolean isDesc) throws JSQLParserException {
        return addUnionOrderByExpression(CCJSqlParserUtil.parseCondExpression(orderByExpression), isDesc);
    }

    /**
     * 添加orderBy语句
     *
     * @param orderByExpression
     * @return
     * @throws JSQLParserException
     */
    public boolean addUnionOrderByExpression(String orderByExpression) throws JSQLParserException {
        return addUnionOrderByExpression(orderByExpression, false);
    }


    /**
     * 设置unionLimit
     *
     * @param rowCount
     */
    public void setUnionLimit(String rowCount) {
        Limit limit = new Limit();
        limit.setRowCount(new LongValue(rowCount));
        unionSelect.setLimit(limit);
    }

    /**
     * 获取unionLimit
     */
    public Limit getUnionLimit() {
        return unionSelect.getLimit();
    }

    /**
     * 设置unionOffset
     *
     * @param start
     */
    public void setUnionOffset(long start) {
        Offset offset = new Offset();
        offset.setOffset(start);
        unionSelect.setOffset(offset);
    }

    /**
     * 获取unionOffset
     */
    public Offset getUnionOffset() {
        return unionSelect.getOffset();
    }

    public PlainSelect getPlainSelect() {
        return plainSelect;
    }

    public void setPlainSelect(PlainSelect plainSelect) {
        if (plainSelect == null) {
            plainSelect = new PlainSelect();
        }
        this.plainSelect = plainSelect;
    }

    public Map<String, Table> getMapJPTable() {
        if (mapJPTable == null) {
            mapJPTable = new HashMap<String, Table>();
        }
        return mapJPTable;
    }

    public void setMapJPTable(Map<String, Table> mapJPTable) {
        this.mapJPTable = mapJPTable;
    }

    public List<Join> getListJoin() {
        if (plainSelect.getJoins() == null) {
            plainSelect.setJoins(new ArrayList<Join>());
        }
        return plainSelect.getJoins();
    }

    public void setListJoin(List<Join> listJoin) {
        plainSelect.setJoins(listJoin);
    }

    public List<OrderByElement> getOrderByElements() {
        if (plainSelect.getOrderByElements() == null) {
            plainSelect.setOrderByElements(new ArrayList<OrderByElement>());
        }
        return plainSelect.getOrderByElements();
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        plainSelect.setOrderByElements(orderByElements);
    }

    public SetOperationList getUnionSelect() {
        return unionSelect;
    }

    public void setUnionSelect(SetOperationList unionSelect) {
        this.unionSelect = unionSelect;
    }

    public List<OrderByElement> getUnionOrderByElements() {
        if (unionSelect.getOrderByElements() == null) {
            unionSelect.setOrderByElements(new ArrayList<OrderByElement>());
        }
        return unionSelect.getOrderByElements();
    }

    @Override
    public String toString() {
        return plainSelect == null ? "" : getSelectBody().toString();
    }

    public SelectBody getSelectBody() {
        getPlainSelect();

        /*if(plainSelect.getSelectItems()==null){
            SelectExpressionItem selectItem = new SelectExpressionItem();
            selectItem.setExpression(new LongValue("*"));
            addSelectItem(selectItem);
        }*/
        if (unionSelect != null && unionSelect.getSelects() != null) {
            return unionSelect;
        }
        /*if(plainSelect.getFromItem()!=null){
            setSubselectBody(unionSelect, null, false);
        }*/
        return plainSelect;
    }

    public static boolean contains(List<?> list, Object object) {
        if (list == null || list.size() == 0) {
            return false;
        }
        for (Object obj : list) {
            if (obj == null) continue;
            if (object.toString().trim().equalsIgnoreCase(obj.toString().trim())) {
                return true;
            }
        }
        return false;
    }
}
