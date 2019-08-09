package com.qinxy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qinxy on 2019/6/24.
 */
@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

//    @ApiModelProperty(value = "发布时间")
//    @TableField(value = "gmt_created")
//    private Date gmtCreated;
//
//    @ApiModelProperty(value = "修改时间")
//    @TableField(value = "gmt_modified")
//    private Date gmtModified;


}
