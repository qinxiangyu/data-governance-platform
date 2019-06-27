package com.weiyi.hlj.entity;

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
 * @since 2019-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="ScheduleTrigger对象", description="")
public class ScheduleTrigger extends BaseEntity {

private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "触发器名称")
    private String triggerName;

    @ApiModelProperty(value = "触发器组名称")
    private String triggerGroup;

    @ApiModelProperty(value = "触发器类型:0-cron,1-h,2-m,3-s")
    private Integer triggerType;

    @ApiModelProperty(value = "trigger_type=0表达式")
    private String cronExpression;

    @ApiModelProperty(value = "trigger_type!=0 触发次数,0表示无限次数")
    private Integer triggerCount;

    @ApiModelProperty(value = "trigger_type!=0 触发间隔,必须为大于0的时间")
    private Integer triggerInterval;


}
