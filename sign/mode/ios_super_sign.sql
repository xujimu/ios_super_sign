/*
Navicat MySQL Data Transfer

Source Server         : 47.103.19.29
Source Server Version : 50732
Source Host           : 47.103.19.29:3306
Source Database       : ios_super_sign

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2020-12-21 15:07:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for apple_iis
-- ----------------------------
DROP TABLE IF EXISTS `apple_iis`;
CREATE TABLE `apple_iis` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `iis` varchar(255) NOT NULL,
  `kid` varchar(255) NOT NULL,
  `cert_id` varchar(255) NOT NULL,
  `identifier` varchar(255) NOT NULL,
  `p8` varchar(255) NOT NULL,
  `p12` varchar(255) NOT NULL COMMENT 'p8文件路径',
  `start` int(11) NOT NULL COMMENT '0不启用,1启用',
  `status` int(11) NOT NULL COMMENT '0失效,未失效',
  `ispublic` int(11) NOT NULL COMMENT '0私,1公',
  `count` int(10) unsigned NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `iis` (`iis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for distribute
-- ----------------------------
DROP TABLE IF EXISTS `distribute`;
CREATE TABLE `distribute` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `page_name` varchar(255) NOT NULL,
  `version` varchar(255) NOT NULL,
  `icon` varchar(255) NOT NULL,
  `ipa` varchar(255) DEFAULT NULL,
  `apk` varchar(255) DEFAULT NULL,
  `url` text,
  `create_time` datetime NOT NULL,
  `introduce` varchar(255) DEFAULT NULL,
  `images` varchar(255) DEFAULT NULL,
  `down_code` int(1) NOT NULL COMMENT '下载码,0不启用,1启用',
  `buy_down_code_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for down_code
-- ----------------------------
DROP TABLE IF EXISTS `down_code`;
CREATE TABLE `down_code` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `down_code` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `use_time` datetime DEFAULT NULL,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for enterprise_sign_cert
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_sign_cert`;
CREATE TABLE `enterprise_sign_cert` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '证书名',
  `cert_path` varchar(255) NOT NULL,
  `moblic_path` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL DEFAULT '' COMMENT '证书状态',
  `count` int(100) unsigned NOT NULL COMMENT '需要扣除的共有池',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `md5` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pack_status
-- ----------------------------
DROP TABLE IF EXISTS `pack_status`;
CREATE TABLE `pack_status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `page_name` varchar(255) DEFAULT NULL,
  `uuid` varchar(32) DEFAULT NULL,
  `udid` varchar(255) DEFAULT NULL,
  `iis` varchar(255) DEFAULT NULL,
  `p12_path` varchar(255) DEFAULT NULL,
  `mobile_path` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ipa` varchar(255) DEFAULT NULL,
  `plist` varchar(255) DEFAULT NULL,
  `status` varchar(25) DEFAULT NULL,
  `sign_off` int(1) DEFAULT NULL,
  `app_id` int(10) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `down_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pack_status_enterprise_sign
-- ----------------------------
DROP TABLE IF EXISTS `pack_status_enterprise_sign`;
CREATE TABLE `pack_status_enterprise_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cert_id` int(10) NOT NULL,
  `cert_name` varchar(255) NOT NULL,
  `account` varchar(255) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `app_name` varchar(255) NOT NULL COMMENT 'app名称',
  `page_name` varchar(255) NOT NULL COMMENT '包名',
  `version` varchar(255) NOT NULL COMMENT '版本',
  `status` varchar(255) DEFAULT NULL COMMENT '打包状态',
  `down_url` varchar(255) DEFAULT NULL COMMENT '下载地址',
  `ipa_path` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pack_status_ios_apk
-- ----------------------------
DROP TABLE IF EXISTS `pack_status_ios_apk`;
CREATE TABLE `pack_status_ios_apk` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(255) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `app_name` varchar(255) NOT NULL COMMENT 'app名称',
  `url` varchar(255) NOT NULL COMMENT '打包网址',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `organization` varchar(255) DEFAULT '' COMMENT '机构',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  `consent_message` varchar(255) DEFAULT '' COMMENT '同意信息',
  `icon` varchar(255) NOT NULL COMMENT '桌面图标',
  `start_icon` varchar(255) DEFAULT NULL COMMENT '启动图路径',
  `is_remove` int(1) NOT NULL COMMENT '是否可移除',
  `is_variable` int(1) DEFAULT NULL,
  `page_name` varchar(255) NOT NULL COMMENT '包名',
  `version` varchar(255) NOT NULL COMMENT '版本',
  `is_xfive` int(1) NOT NULL COMMENT '是否集成x5',
  `status` varchar(255) NOT NULL DEFAULT '' COMMENT '打包状态',
  `preview` varchar(255) DEFAULT NULL COMMENT '预览地址',
  `down` varchar(255) DEFAULT NULL COMMENT '源码下载地址',
  `expiration_time` datetime DEFAULT NULL COMMENT '下载过期时间',
  `root_cert` varchar(255) DEFAULT NULL COMMENT '根证书',
  `server_cert` varchar(255) DEFAULT NULL COMMENT '根证书',
  `key_cert` varchar(255) DEFAULT NULL COMMENT '证书秘钥',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for software_distribute
-- ----------------------------
DROP TABLE IF EXISTS `software_distribute`;
CREATE TABLE `software_distribute` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `page_name` varchar(255) NOT NULL,
  `version` varchar(255) NOT NULL,
  `icon` varchar(255) NOT NULL,
  `ipa` varchar(255) DEFAULT NULL,
  `apk` varchar(255) DEFAULT NULL,
  `url` text,
  `create_time` datetime NOT NULL,
  `introduce` varchar(255) DEFAULT NULL,
  `uuid` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `type` int(1) unsigned NOT NULL COMMENT '0普通用户1管理员',
  `count` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4;
