DROP TABLE IF EXISTS `data_source_entity`;
CREATE TABLE `data_source_entity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(48) DEFAULT NULL,
  `password` varchar(48) DEFAULT NULL,
  `url` varchar(256) DEFAULT NULL,
  `driver_class_name` varchar(48) DEFAULT NULL,
  `code` varchar(48) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;