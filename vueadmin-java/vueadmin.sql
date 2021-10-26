/*
 Navicat Premium Data Transfer

 Source Server         : localhost_本地
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost
 Source Database       : vueadmin

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : utf-8

 Date: 10/26/2021 14:02:20 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` longtext,
  `ip` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1452877894664634371 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `sys_log`
-- ----------------------------
BEGIN;
INSERT INTO `sys_log` VALUES ('1452875323237588993', 'anonymousUser', '获取验证码', 'com.yl.controller.AuthController.captcha', '[]', '0:0:0:0:0:0:0:1', '2021-10-26 13:50:45', null, null), ('1452875323237588994', 'anonymousUser', '获取验证码', 'com.yl.controller.AuthController.captcha', '[]', '0:0:0:0:0:0:0:1', '2021-10-26 13:50:45', null, null), ('1452875326525923330', 'anonymousUser', '获取验证码', 'com.yl.controller.AuthController.captcha', '[]', '0:0:0:0:0:0:0:1', '2021-10-26 13:50:46', null, null), ('1452875364333379585', 'admin', '登陆成功', 'login', null, '0:0:0:0:0:0:0:1', '2021-10-26 13:50:55', null, null), ('1452877860032266242', 'anonymousUser', '获取验证码', 'com.yl.controller.AuthController.captcha', '[]', '0:0:0:0:0:0:0:1', '2021-10-26 14:00:50', null, null), ('1452877864566308865', 'anonymousUser', '获取验证码', 'com.yl.controller.AuthController.captcha', '[]', '0:0:0:0:0:0:0:1', '2021-10-26 14:00:51', null, null), ('1452877894664634370', 'admin', '登陆成功', 'login', null, '0:0:0:0:0:0:0:1', '2021-10-26 14:00:58', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(64) NOT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(255) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `component` varchar(255) DEFAULT NULL,
  `type` int NOT NULL COMMENT '类型     0：目录   1：菜单   2：按钮',
  `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `orderNum` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1452877598907482114 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', '', 'sys:manage', '', '0', 'el-icon-s-operation', '1', '2021-01-15 18:58:18', '2021-01-15 18:58:20', '1'), ('2', '1', '用户管理', '/sys/users', 'sys:user:list', 'sys/User', '1', 'el-icon-s-custom', '1', '2021-01-15 19:03:45', '2021-01-15 19:03:48', '1'), ('3', '1', '角色管理', '/sys/roles', 'sys:role:list', 'sys/Role', '1', 'el-icon-rank', '2', '2021-01-15 19:03:45', '2021-01-15 19:03:48', '1'), ('4', '1', '菜单管理', '/sys/menus', 'sys:menu:list', 'sys/Menu', '1', 'el-icon-menu', '3', '2021-01-15 19:03:45', '2021-01-15 19:03:48', '1'), ('5', '0', '系统工具', '', 'sys:tools', null, '0', 'el-icon-s-tools', '2', '2021-01-15 19:06:11', null, '1'), ('6', '5', '数字字典', '/sys/dicts', 'sys:dict:list', 'sys/Dict', '1', 'el-icon-s-order', '1', '2021-01-15 19:07:18', '2021-01-18 16:32:13', '1'), ('7', '3', '添加角色', '', 'sys:role:save', '', '2', '', '1', '2021-01-15 23:02:25', '2021-01-17 21:53:14', '0'), ('9', '2', '添加用户', null, 'sys:user:save', null, '2', null, '1', '2021-01-17 21:48:32', null, '1'), ('10', '2', '修改用户', null, 'sys:user:update', null, '2', null, '2', '2021-01-17 21:49:03', '2021-01-17 21:53:04', '1'), ('11', '2', '删除用户', null, 'sys:user:delete', null, '2', null, '3', '2021-01-17 21:49:21', null, '1'), ('12', '2', '分配角色', null, 'sys:user:role', null, '2', null, '4', '2021-01-17 21:49:58', null, '1'), ('13', '2', '重置密码', null, 'sys:user:repass', null, '2', null, '5', '2021-01-17 21:50:36', null, '1'), ('14', '3', '修改角色', null, 'sys:role:update', null, '2', null, '2', '2021-01-17 21:51:14', null, '1'), ('15', '3', '删除角色', null, 'sys:role:delete', null, '2', null, '3', '2021-01-17 21:51:39', null, '1'), ('16', '3', '分配权限', null, 'sys:role:perm', null, '2', null, '5', '2021-01-17 21:52:02', null, '1'), ('17', '4', '添加菜单', null, 'sys:menu:save', null, '2', null, '1', '2021-01-17 21:53:53', '2021-01-17 21:55:28', '1'), ('18', '4', '修改菜单', null, 'sys:menu:update', null, '2', null, '2', '2021-01-17 21:56:12', null, '1'), ('19', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', '2021-01-17 21:56:36', null, '1'), ('1452877598907482113', '1', '系统日志', 'sys/log', 'sys:log:findList', null, '1', null, '2', '2021-10-26 13:59:47', null, '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `code` varchar(64) NOT NULL,
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('3', '普通用户', 'normal', '只有基本查看功能', '2021-01-04 10:09:14', '2021-01-30 08:19:52', '1'), ('6', '超级管理员', 'admin', '系统默认最高权限，不可以编辑和任意修改', '2021-01-16 13:29:03', '2021-01-17 15:50:45', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `sys_role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('96', '3', '1'), ('97', '3', '2'), ('98', '3', '3'), ('99', '3', '4'), ('100', '3', '5'), ('101', '3', '6'), ('118', '6', '1'), ('119', '6', '2'), ('120', '6', '9'), ('121', '6', '10'), ('122', '6', '11'), ('123', '6', '12'), ('124', '6', '13'), ('125', '6', '3'), ('126', '6', '7'), ('127', '6', '14'), ('128', '6', '15'), ('129', '6', '16'), ('130', '6', '4'), ('131', '6', '17'), ('132', '6', '18'), ('133', '6', '19');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USERNAME` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$R7zegeWzOXPw871CmNuJ6upC0v8D373GuLuTw8jn6NET4BkPRZfgK', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', '123@qq.com', '广州', '2021-01-12 22:13:53', '2021-01-16 16:57:32', '2020-12-30 08:38:37', '1'), ('2', 'test', '$2a$10$glr/BsWsbUIIHfQ988AVQ.gnv2sKN7dyLjXNa9E2JkJgpZJWX/5Ji', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', 'test@qq.com', null, '2021-01-30 08:20:22', '2021-06-07 10:29:21', null, '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `sys_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('13', '2', '3'), ('14', '1', '6');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
