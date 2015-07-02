/*==============================================================*/
/* Table: t_g_svn_branch_config                                 */
/*==============================================================*/
CREATE TABLE t_g_svn_branch_config (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_code varchar(50) DEFAULT NULL,
  branch_url varchar(300) DEFAULT NULL,
  branch_name varchar(50) DEFAULT NULL,
  create_by varchar(30) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  create_version varchar(30) DEFAULT NULL,
  current_version varchar(30) DEFAULT NULL,
  status smallint(6) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
/*==============================================================*/
/* Table: t_g_client_config                                     */
/*==============================================================*/
CREATE TABLE t_g_client_config (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_code varchar(30) DEFAULT NULL,
  profile varchar(30) DEFAULT NULL,
  remote_ip varchar(30) DEFAULT NULL,
  status int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
﻿/*==============================================================*/
/* Table: t_g_project_status                                    */
/*==============================================================*/
CREATE TABLE t_g_project_status (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_code varchar(30) DEFAULT NULL,
  current_status int(11) DEFAULT NULL,
  process_rate int(11) DEFAULT NULL,
  operate_staff varchar(30) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  finish_time datetime DEFAULT NULL,
  result_info text,
  profile varchar(30) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_project_status_history                            */
/*==============================================================*/
create table t_g_project_status_history 
(
  id bigint(20) DEFAULT NULL,
  project_code varchar(30) DEFAULT NULL,
  current_status int(11) DEFAULT NULL,
  process_rate int(11) DEFAULT NULL,
  operate_staff varchar(30) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  finish_time datetime DEFAULT NULL,
  result_info text,
  profile varchar(30) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_user                                              */
/*==============================================================*/
CREATE TABLE t_g_user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  password varchar(50) DEFAULT NULL,
  real_name varchar(50) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  create_by varchar(30) DEFAULT NULL,
  status int(11) DEFAULT NULL,
  login_time datetime DEFAULT NULL,
  last_login_time datetime DEFAULT NULL,
  depart_name varchar(100) DEFAULT NULL,
  is_admin int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY user_name_UNIQUE (user_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_project                                           */
/*==============================================================*/
CREATE TABLE t_g_project (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_code varchar(30) DEFAULT NULL,
  project_name varchar(30) DEFAULT NULL,
  repository_url varchar(200) DEFAULT NULL,
  create_by varchar(30) DEFAULT NULL,
  manager varchar(30) DEFAULT NULL,
  status int(11) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY project_code_UNIQUE (project_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_function_right                                    */
/*==============================================================*/
CREATE TABLE t_g_function_right (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  project_code varchar(30) DEFAULT NULL,
  status int(11) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  create_by varchar(30) DEFAULT NULL,
  real_name varchar(50) DEFAULT NULL,
  project_name varchar(30) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


/*==============================================================*/
/* Table: t_g_properties_config                                 */
/*==============================================================*/
CREATE TABLE t_g_properties_config (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_code varchar(30) DEFAULT NULL,
  profile varchar(30) DEFAULT NULL,
  pro_key varchar(30) DEFAULT NULL,
  pro_value varchar(500) DEFAULT NULL,
  remark varchar(50) DEFAULT NULL,
  create_by varchar(30) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  last_value varchar(500) DEFAULT NULL,
  status int(11) DEFAULT NULL,
  auditor varchar(30) DEFAULT NULL,
  auditor_text varchar(100) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_project_private                                   */
/*==============================================================*/
CREATE TABLE t_g_project_private (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  project_code varchar(30) DEFAULT NULL,
  virtual_truck_url varchar(200) DEFAULT NULL,
  if_virtual int(11) DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_operate_logs                                      */
/*==============================================================*/
CREATE TABLE t_g_operate_logs (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  project_code varchar(30) DEFAULT NULL,
  profile varchar(30) DEFAULT NULL,
  operate_code int(11) DEFAULT NULL,
  execute_time datetime DEFAULT NULL,
  execute_result int(11) DEFAULT NULL,
  result_info text,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_svn_command_logs                                  */
/*==============================================================*/
CREATE TABLE t_g_maven_command_logs (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  project_code varchar(30) DEFAULT NULL,
  commands varchar(500) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  result_info varchar(200) DEFAULT NULL,
  profile varchar(30) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_svn_conflict                                      */
/*==============================================================*/
CREATE TABLE t_g_svn_conflict (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  project_code varchar(30) DEFAULT NULL,
  branch_url varchar(200) DEFAULT NULL,
  trunk_url varchar(200) DEFAULT NULL,
  file_name varchar(200) DEFAULT NULL,
  status int(11) DEFAULT NULL,
  result_info text,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_svn_change_logs                                   */
/*==============================================================*/
CREATE TABLE t_g_svn_change_logs (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  type int(11) DEFAULT NULL,
  repository_url varchar(200) DEFAULT NULL,
  file_name varchar(300) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  result_status int(11) DEFAULT NULL,
  result_info text,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/*==============================================================*/
/* Table: t_g_maven_command_logs                                */
/*==============================================================*/
CREATE TABLE t_g_svn_command_logs (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  repository_url varchar(200) DEFAULT NULL,
  commands varchar(300) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  real_name varchar(30) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

