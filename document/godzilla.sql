/*
Navicat MySQL Data Transfer

Source Server         : 10.100.142.11
Source Server Version : 50520
Source Host           : 10.100.142.11:3306
Source Database       : godzilla_db

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2015-10-12 10:02:21
*/

SET FOREIGN_KEY_CHECKS=0;


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
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(30) DEFAULT NULL,
  `project_name` varchar(30) DEFAULT NULL,
  `repository_url` varchar(200) DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `manager` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `checkout_path` varchar(255) DEFAULT NULL,
  `web_path` varchar(255) DEFAULT NULL,
  `war_name` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `svn_username` varchar(255) DEFAULT NULL,
  `svn_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_code_UNIQUE` (`project_code`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for properties_config
-- ----------------------------
DROP TABLE IF EXISTS `properties_config`;
CREATE TABLE `properties_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
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
) ENGINE=InnoDB AUTO_INCREMENT=3004 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for operate_logs
-- ----------------------------
DROP TABLE IF EXISTS `operate_logs`;
CREATE TABLE `operate_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) DEFAULT NULL,
  `project_code` varchar(30) DEFAULT NULL,
  `profile` varchar(30) DEFAULT NULL,
  `client_ip` varchar(50) DEFAULT NULL,
  `sort` varchar(3) DEFAULT NULL,
  `commands` varchar(3000) DEFAULT NULL,
  `operation` varchar(50) DEFAULT NULL,
  `operate_code` int(11) DEFAULT NULL,
  `execute_time` datetime DEFAULT NULL,
  `execute_result` int(11) DEFAULT NULL,
  `result_info` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1719 DEFAULT CHARSET=utf8;
