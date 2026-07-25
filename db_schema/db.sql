-- 用户登录账号表 --
DROP TABLE IF EXISTS `sys_login_user`;
CREATE TABLE `sys_login_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户唯一主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID,唯一',
  `account` VARCHAR(50) NOT NULL COMMENT '登录账号，唯一（用户名/手机号/邮箱）',
  `password` VARCHAR(128) NOT NULL COMMENT '加密密码（BCrypt加密，不存明文）',
  `salt` VARCHAR(64) DEFAULT '' COMMENT '加密盐值（BCrypt可省略，预留兼容）',
  `nickname` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '账号展示昵称',
  `avatar` VARCHAR(255) DEFAULT '' COMMENT '默认头像地址',
  `email` VARCHAR(100) DEFAULT '' COMMENT '绑定邮箱，可用于登录/找回密码',
  `phone` VARCHAR(20) DEFAULT '' COMMENT '绑定手机号，可用于登录',
  `lock_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '锁定标记：0未锁定 1锁定',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`,`deleted`) COMMENT '唯一，排除已删除数据',
  UNIQUE KEY `uk_account` (`account`,`deleted`) COMMENT '账号唯一，排除已删除数据'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录账号表';

-- 用户信息表 --
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` BIGINT NOT NULL COMMENT '关联账号表user_id，一对一唯一',
  `real_name` VARCHAR(30) DEFAULT '' COMMENT '真实姓名',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
  `birthday` DATE DEFAULT NULL COMMENT '出生日期',
  `birth_time` VARCHAR(20) DEFAULT NULL COMMENT '出生时间',
  `province` VARCHAR(20) DEFAULT '' COMMENT '省份',
  `city` VARCHAR(20) DEFAULT '' COMMENT '城市',
  `district` VARCHAR(20) DEFAULT '' COMMENT '区/县',
  `address` VARCHAR(255) DEFAULT '' COMMENT '详细收货/居住地址',
  `extra_json` JSON DEFAULT NULL COMMENT '扩展自定义字段（无需加字段直接存JSON）',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`,`deleted`) COMMENT '一个账号对应一条资料',
  KEY `idx_real_name` (`real_name`) COMMENT '姓名检索索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- 操作审计日志表 --
DROP TABLE IF EXISTS `sys_audit_log`;
CREATE TABLE `sys_audit_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
  `account` VARCHAR(50) DEFAULT '' COMMENT '操作用户账号',
  `module` VARCHAR(50) DEFAULT '' COMMENT '所属模块',
  `operation_type` VARCHAR(20) DEFAULT '' COMMENT '操作类型',
  `description` VARCHAR(255) DEFAULT '' COMMENT '操作描述',
  `request_method` VARCHAR(10) DEFAULT '' COMMENT '请求方式',
  `request_uri` VARCHAR(255) DEFAULT '' COMMENT '请求地址',
  `request_ip` VARCHAR(50) DEFAULT '' COMMENT '请求IP',
  `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
  `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
  `success` TINYINT DEFAULT 1 COMMENT '是否成功：0失败 1成功',
  `error_message` VARCHAR(500) DEFAULT '' COMMENT '错误信息',
  `duration` BIGINT DEFAULT 0 COMMENT '执行时长（毫秒）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) COMMENT '按用户查询',
  KEY `idx_create_time` (`create_time`) COMMENT '按时间查询',
  KEY `idx_module` (`module`) COMMENT '按模块查询'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作审计日志表';
