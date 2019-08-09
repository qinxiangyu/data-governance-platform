package com.qinxy.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by qinxy on 2019/7/10.
 */
@Data
public class SQLParserDTO extends BaseDTO {

    private String datasource;

    private String table;

    private LinkedHashMap<String,String> joins;

    private List<String> conditions;

    private List<String> having;

    private List<String> outColumns;

    private String limit;

    private List<String> groupByColumns;

}
