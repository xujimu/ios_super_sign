/*
 Navicat MySQL Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 127.0.0.1:3306
 Source Schema         : ios_super_sign

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 03/05/2022 20:55:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apple_iis
-- ----------------------------
CREATE TABLE IF NOT EXISTS `apple_iis` (
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `iis` (`iis`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for distribute
-- ----------------------------
CREATE TABLE IF NOT EXISTS `distribute` (
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
  `language` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1651482310 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for domain
-- ----------------------------
CREATE TABLE IF NOT EXISTS `domain` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `domain` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for down_code
-- ----------------------------
CREATE TABLE IF NOT EXISTS `down_code` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `down_code` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `use_time` datetime DEFAULT NULL,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for enterprise_sign_cert
-- ----------------------------
CREATE TABLE IF NOT EXISTS `enterprise_sign_cert` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ios_sign_software_distribute
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ios_sign_software_distribute` (
  `ios_id` varchar(40) NOT NULL,
  `account` varchar(255) NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `page_name` varchar(255) NOT NULL,
  `version` varchar(255) NOT NULL,
  `icon` varchar(255) NOT NULL,
  `ipa` varchar(255) DEFAULT NULL,
  `apk` varchar(255) DEFAULT NULL,
  `url` text,
  `cert_id` varchar(40) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL,
  `introduce` varchar(255) DEFAULT NULL,
  `auto_page_name` int(10) unsigned NOT NULL COMMENT '自动更换包名 0否 1是',
  PRIMARY KEY (`ios_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='ipa自助分发';

-- ----------------------------
-- Table structure for ios_sign_software_distribute_status
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ios_sign_software_distribute_status` (
  `uuid` varchar(40) CHARACTER SET utf8mb4 NOT NULL,
  `account` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `ios_id` varchar(40) CHARACTER SET utf8mb4 NOT NULL,
  `cert_id` varchar(40) CHARACTER SET utf8mb4 NOT NULL,
  `app_name` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `app_version` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `page_name` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `down_url` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `status` varchar(40) CHARACTER SET utf8mb4 NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ios_sign_udid_cert
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ios_sign_udid_cert` (
  `cert_id` varchar(40) NOT NULL,
  `account` varchar(255) NOT NULL,
  `p12_path` varchar(255) NOT NULL COMMENT 'p8文件路径',
  `mobileprovision_path` varchar(255) NOT NULL,
  `p12_password` varchar(255) NOT NULL,
  `udid` varchar(255) NOT NULL,
  `introduce` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`cert_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for mdm_distribute
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_distribute` (
  `id` varchar(64) NOT NULL,
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
  `language` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for mdm_down_code
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_down_code` (
  `id` varchar(64) NOT NULL,
  `account` varchar(255) NOT NULL,
  `down_code` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `use_time` datetime DEFAULT NULL,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for mdm_pack_status
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_pack_status` (
  `id` varchar(64) NOT NULL,
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
  `app_id` varchar(64) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `down_code` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for mdm_software_distribute
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_software_distribute` (
  `uuid` varchar(40) NOT NULL,
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
  `language` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for mdm_software_distribute_down_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_software_distribute_down_record` (
  `device_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `app_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for mdm_software_distribute_down_record_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_software_distribute_down_record_info` (
  `record_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `uuid` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'appid',
  `app_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'app名字',
  `app_page_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'app包名',
  `udid` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '设备udid',
  `ip` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '下载ip',
  `create_time` datetime NOT NULL COMMENT '下载时间',
  `account` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户账号',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='mdm企业分发 下载详细记录';

-- ----------------------------
-- Table structure for mdm_super_update_ipa_task
-- ----------------------------

CREATE TABLE IF NOT EXISTS `mdm_super_update_ipa_task` (
  `uuid` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `pack_status_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '打包任务id',
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '状态 待处理 已处理 失败',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `plist_url` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `task_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '任务id',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for pack_status
-- ----------------------------

CREATE TABLE IF NOT EXISTS `pack_status` (
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for pack_status_enterprise_sign
-- ----------------------------

CREATE TABLE IF NOT EXISTS `pack_status_enterprise_sign` (
  `id` varchar(64) NOT NULL COMMENT 'id',
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
  `is_time_lock` int(11) NOT NULL COMMENT '是否开启时间锁 0关1开',
  `lock_time_finish` datetime DEFAULT NULL COMMENT '到期时间',
  `lock_request_url` text COMMENT '时间锁请求url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for pack_status_ios_apk
-- ----------------------------

CREATE TABLE IF NOT EXISTS `pack_status_ios_apk` (
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for settings
-- ----------------------------

CREATE TABLE IF NOT EXISTS `settings` (
  `time_lock_request_url` text NOT NULL COMMENT '时间锁请求url'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设置';

-- ----------------------------
-- Table structure for software_distribute
-- ----------------------------

CREATE TABLE IF NOT EXISTS `software_distribute` (
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
  `language` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for software_distribute_down_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `software_distribute_down_record` (
  `record_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `app_id` int(10) NOT NULL COMMENT 'appid',
  `app_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'app名字',
  `app_page_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'app包名',
  `ip` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '下载ip',
  `create_time` datetime NOT NULL COMMENT '下载时间',
  `account` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户账号',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='mdm企业分发 下载详细记录';

-- ----------------------------
-- Table structure for systemctl_settings
-- ----------------------------

CREATE TABLE IF NOT EXISTS `systemctl_settings` (
  `mdm_soft_num` int(10) DEFAULT NULL COMMENT ' mdm企业签第几次下载触发',
  `mdm_soft_re_count` int(10) DEFAULT NULL COMMENT '额外扣除扣除数量',
  `mdm_super_num` int(10) DEFAULT NULL COMMENT ' mdm超级签第几次下载触发',
  `mdm_super_re_count` int(11) DEFAULT NULL COMMENT '扣除数量',
  `super_num` int(10) DEFAULT NULL COMMENT '超级签第几次下载触发',
  `super_re_count` int(10) DEFAULT NULL COMMENT '扣除数量',
  `soft_num` int(11) DEFAULT NULL COMMENT '企业签第几次下载触发',
  `soft_re_count` int(11) DEFAULT NULL COMMENT '额外扣除扣除数量',
  `super_total` int(11) DEFAULT NULL COMMENT '超级签所需公有池',
  `mdm_super_total` int(11) DEFAULT NULL COMMENT 'mdm超级签共有池',
  `soft_total` int(11) DEFAULT NULL COMMENT '企业分发公有池',
  `mdm_soft_total` int(11) DEFAULT NULL COMMENT 'mdm企业分发公有池',
  `web_pack_total` int(11) DEFAULT NULL COMMENT '网页打包所需公有池',
  `one_super_total` int(11) DEFAULT NULL COMMENT '单点分发扣除次数',
  `mq_domain` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '免签打包域名',
  `mdm_domain` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'mdm域名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for test
-- ----------------------------

CREATE TABLE IF NOT EXISTS `test` (
  `test` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `type` int(1) unsigned NOT NULL COMMENT '0普通用户1管理员',
  `count` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `account` (`account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
