package com.weiyi.hlj.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.weiyi.hlj.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author system
 * @since 2019-06-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="User对象", description="")
public class User extends BaseEntity {

private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;


}
