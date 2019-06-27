DROP TABLE IF EXISTS `data_source_entity`;
CREATE TABLE `data_source_entity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(48) DEFAULT NULL,
  `password` varchar(48) DEFAULT NULL,
  `url` varchar(256) DEFAULT NULL,
  `driver_class_name` varchar(48) DEFAULT NULL,
  `code` varchar(48) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/**任务表**/
drop table if exists schedule_job;
create table if not exists schedule_job(
  id bigint primary key AUTO_INCREMENT comment '主键',
  job_alias varchar(50) not null comment '任务别名',
  job_name varchar(50) not null unique comment '任务名称',
  job_group varchar(50) not null comment '任务组名称',
  job_impl varchar(100) not null comment '任务完整类名:a.b.Clazz',
  job_enable bit not null default false comment '定时任务是否启用',
  trigger_id bigint comment '触发器ID',
  job_params varchar(200) comment '任务参数',
  job_uuid varchar(32) comment '任务唯一标识'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

/**触发器表**/
drop table if exists schedule_trigger;
create table if not exists schedule_trigger(
  id bigint primary key AUTO_INCREMENT comment '主键',
  trigger_name varchar(50) not null unique comment '触发器名称',
  trigger_group varchar(50) not null comment '触发器组名称',
  trigger_type int not null comment '触发器类型:0-cron,1-h,2-m,3-s',
  cron_expression varchar(10) comment 'trigger_type=0表达式',
  trigger_count int comment 'trigger_type!=0 触发次数,0表示无限次数',
  trigger_interval int comment 'trigger_type!=0 触发间隔,必须为大于0的时间'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;