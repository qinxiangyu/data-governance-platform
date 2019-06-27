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
@ApiModel(value="ScheduleJob对象", description="")
public class ScheduleJob extends BaseEntity {

private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "任务别名")
    private String jobAlias;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名称")
    private String jobGroup;

    @ApiModelProperty(value = "任务完整类名:a.b.Clazz")
    private String jobImpl;

    @ApiModelProperty(value = "定时任务是否启用")
    private Boolean jobEnable;

    @ApiModelProperty(value = "触发器ID")
    private Long triggerId;

    @ApiModelProperty(value = "任务参数")
    private String jobParams;

    @ApiModelProperty(value = "任务唯一标识")
    private String jobUuid;


}
