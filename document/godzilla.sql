/*
Navicat MySQL Data Transfer

Source Server         : 10.100.142.11
Source Server Version : 50520
Source Host           : 10.100.142.11:3306
Source Database       : godzilla2

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2015-11-05 10:17:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for client_config
-- ----------------------------
DROP TABLE IF EXISTS `client_config`;
CREATE TABLE `client_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(30) DEFAULT NULL,
  `profile` varchar(30) DEFAULT NULL,
  `deploy_version` varchar(255) DEFAULT NULL,
  `remote_ip` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tomcat_password` varchar(255) DEFAULT NULL,
  `tomcat_username` varchar(255) DEFAULT NULL,
  `tomcat_port` varchar(255) DEFAULT NULL,
  `tomcat_need_plugin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for function_right
-- ----------------------------
DROP TABLE IF EXISTS `function_right`;
CREATE TABLE `function_right` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) DEFAULT NULL,
  `project_code` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `project_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=488 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for operate_logs
-- ----------------------------
DROP TABLE IF EXISTS `operate_logs`;
CREATE TABLE `operate_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) DEFAULT NULL,
  `real_name` varchar(30) DEFAULT NULL,
  `project_code` varchar(30) DEFAULT NULL,
  `profile` varchar(30) DEFAULT NULL,
  `client_ip` varchar(50) DEFAULT NULL,
  `sort` varchar(30) DEFAULT NULL,
  `commands` varchar(3000) DEFAULT NULL,
  `operation` varchar(50) DEFAULT NULL,
  `operate_code` int(11) DEFAULT NULL,
  `execute_time` datetime DEFAULT NULL,
  `execute_result` int(11) DEFAULT NULL,
  `result_info` text,
  `deploy_log` longtext,
  `war_info` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2651 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `svn_conflict_id` bigint(20) DEFAULT NULL,
  `merge_status` varchar(11) DEFAULT '0' COMMENT '0:无 1:有 2:标记解决',
  `project_code` varchar(30) DEFAULT NULL,
  `project_name` varchar(30) DEFAULT NULL,
  `repository_url` varchar(200) DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `manager` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `checkout_path` varchar(255) DEFAULT NULL,
  `web_path` varchar(255) DEFAULT NULL,
  `lib_path` varchar(700) DEFAULT NULL,
  `war_name` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `svn_username` varchar(255) DEFAULT NULL,
  `svn_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_code_UNIQUE` (`project_code`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for properties_bill
-- ----------------------------
DROP TABLE IF EXISTS `properties_bill`;
CREATE TABLE `properties_bill` (
  `id` bigint(20) NOT NULL,
  `createby` varchar(255) DEFAULT NULL,
  `project_code` varchar(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `auditor_text` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for properties_config
-- ----------------------------
DROP TABLE IF EXISTS `properties_config`;
CREATE TABLE `properties_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) DEFAULT NULL,
  `project_code` varchar(30) DEFAULT NULL,
  `profile` varchar(30) DEFAULT NULL,
  `pro_key` varchar(500) DEFAULT NULL,
  `pro_value` varchar(5000) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `last_value` varchar(500) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0:未审批\r\n1:通过\r\n2:未通过\r\n\r\n',
  `auditor` varchar(30) DEFAULT NULL,
  `auditor_text` varchar(100) DEFAULT NULL,
  `index_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6754 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for svn_branch_config
-- ----------------------------
DROP TABLE IF EXISTS `svn_branch_config`;
CREATE TABLE `svn_branch_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) DEFAULT NULL,
  `branch_url` varchar(300) DEFAULT NULL,
  `branch_name` varchar(50) DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_version` varchar(30) DEFAULT NULL,
  `current_version` varchar(30) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for svn_conflict
-- ----------------------------
DROP TABLE IF EXISTS `svn_conflict`;
CREATE TABLE `svn_conflict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(500) DEFAULT NULL,
  `profile` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(100) DEFAULT NULL,
  `conflict_path` varchar(500) DEFAULT NULL,
  `version` varchar(50) DEFAULT NULL,
  `conflict_files` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `depart_name` varchar(100) DEFAULT NULL,
  `is_admin` int(11) DEFAULT NULL,
  `is_deployer` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
