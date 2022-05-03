/*
 Navicat MySQL Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 127.0.0.1:3306
 Source Schema         : mdm

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 03/05/2022 20:55:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cert_info
-- ----------------------------
DROP TABLE IF EXISTS `cert_info`;
CREATE TABLE `cert_info` (
  `cert_id` varchar(64) NOT NULL COMMENT '证书id',
  `p12_path` varchar(255) NOT NULL COMMENT '证书路径 如 ./data/p12.p12',
  `cert_name` varchar(1000) NOT NULL COMMENT '证书名',
  `p12_password` varchar(255) NOT NULL COMMENT '证书密码',
  `cert_status` int(10) NOT NULL COMMENT '状态 1正常 0失效 ',
  `topic` varchar(255) NOT NULL COMMENT '证书信息 topic 推送需要使用',
  `serial_number` varchar(255) NOT NULL COMMENT '证书信息serial_number',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `start_time` datetime NOT NULL COMMENT '证书有效期起始时间',
  `end_time` datetime NOT NULL COMMENT '证书有效期结束时间',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `mobile_config_path` varchar(255) NOT NULL COMMENT '配置文件路径',
  PRIMARY KEY (`cert_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='mdm证书表';

-- ----------------------------
-- Table structure for device_command_task
-- ----------------------------
DROP TABLE IF EXISTS `device_command_task`;
CREATE TABLE `device_command_task` (
  `task_id` varchar(64) NOT NULL COMMENT 'id',
  `device_id` varchar(64) NOT NULL COMMENT '设备id',
  `cmd` varchar(255) NOT NULL COMMENT '命令 ',
  `exec_result` text NOT NULL COMMENT '执行返回结果',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `exec_time` datetime NOT NULL COMMENT '执行时间',
  `result_time` datetime NOT NULL COMMENT '返回时间',
  `task_status` int(1) NOT NULL COMMENT '0 任务_等待_执行 1 任务_唤醒_命令_已发送 2 任务_命令_已发送 3任务_命令_执行成功 4 任务_命令_执行失败',
  `push_count` int(2) NOT NULL COMMENT '唤醒次数 如果在指定时间没有回应则会重试 ',
  `exec_result_status` varchar(255) NOT NULL COMMENT '执行返回状态码',
  `cmd_append` text NOT NULL COMMENT 'cmd命令的其他参数 json 比如安装app就有其他参数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cert_id` varchar(64) NOT NULL COMMENT '证书id',
  `udid` varchar(64) NOT NULL COMMENT '设备id',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='执行命令任务';

-- ----------------------------
-- Table structure for device_info
-- ----------------------------
DROP TABLE IF EXISTS `device_info`;
CREATE TABLE `device_info` (
  `device_id` varchar(64) NOT NULL COMMENT '设备唯一id',
  `cert_id` varchar(64) DEFAULT '' COMMENT '证书id',
  `token` varchar(1000) DEFAULT '' COMMENT '设备token',
  `udid` varchar(255) DEFAULT '' COMMENT '设备uuid',
  `unlock_token` text COMMENT '给设备发送命令配置文件里需要用到',
  `magic` varchar(255) DEFAULT '' COMMENT '向apns服务器唤醒设备的时候需要',
  `topic` varchar(255) DEFAULT '' COMMENT '证书的topic',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` varchar(255) DEFAULT '' COMMENT '设备状态 已卸载CheckOut 注册中Authenticate 更新TokenUpdate  ',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='注册的设备信息';

-- ----------------------------
-- Table structure for device_status
-- ----------------------------
DROP TABLE IF EXISTS `device_status`;
CREATE TABLE `device_status` (
  `device_id` varchar(64) NOT NULL COMMENT '设备id',
  `status` int(1) NOT NULL COMMENT '0不可控 1可控',
  `udid` varchar(64) NOT NULL COMMENT '设备udid',
  `cert_id` varchar(64) NOT NULL COMMENT '证书id',
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
