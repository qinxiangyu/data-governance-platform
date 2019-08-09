package com.qinxy.dto;

import lombok.Data;

/**
 * Created by qinxy on 2019/7/23.
 */
@Data
public class SQLGenerateDTO extends BaseDTO {
    private String select;

    private String table;

    private String where;

}
