/*==============================================================*/
/* Table: t_g_project_status                                    */
/*==============================================================*/
create table t_g_project_status 
(
   id                   numeric,
   project_code         varchar(30),
   current_status       int,
   process_rate         int,
   operate_staff        varchar(30),
   create_time          datetime,
   update_time          datetime,
   finish_time          datetime,
   result_info          text
);

/*==============================================================*/
/* Table: t_g_project_status_history                            */
/*==============================================================*/
create table t_g_project_status_history 
(
   id                   char(10),
   project_code         varchar(30),
   current_status       int,
   "process_rate(%)"    int,
   operate_staff        varchar(30),
   create_time          datetime,
   update_time          datetime,
   finish_time          datetime,
   result_info          text
);

/*==============================================================*/
/* Table: t_g_user                                              */
/*==============================================================*/
create table t_g_user 
(
   id                   numeric,
   user_name            varchar(30),
   password             varchar(50),
   real_name            varchar(50),
   create_time          datetime,
   create_by            varchar(30),
   status               int,
   login_time           datetime,
   last_login_time      datetime,
   depart_name          varchar(100),
   is_admin             int
);

/*==============================================================*/
/* Table: t_g_project                                           */
/*==============================================================*/
create table t_g_project 
(
   id                   numeric,
   project_code         varchar(30),
   project_name         varchar(30),
   repository_url       varchar(200),
   create_by            varchar(30),
   manager              varchar(30),
   status               int
);

/*==============================================================*/
/* Table: t_g_function_right                                    */
/*==============================================================*/
create table t_g_function_right 
(
   id                   numeric,
   user_name            varchar(30),
   project_code         varchar(30),
   status               int,
   create_time          datetime,
   update_time          datetime,
   create_by            varchar(30),
   real_name            varchar(50),
   project_name         varchar(30)
);


/*==============================================================*/
/* Table: t_g_properties_config                                 */
/*==============================================================*/
create table t_g_properties_config 
(
   id                   numeric,
   project_code         varchar(30),
   profile              varchar(30),
   pro_key              varchar(30),
   pro_value            varchar(500),
   remark               varchar(50),
   create_by            varchar(30),
   create_time          datetime,
   update_time          datetime,
   last_value           varchar(500),
   status               int,
   auditor              varchar(30),
   auditor_text         varchar(100)
);

/*==============================================================*/
/* Table: t_g_project_private                                   */
/*==============================================================*/
create table t_g_project_private 
(
   id                   char(10),
   user_name            varchar(30),
   project_code         varchar(30),
   virtual_truck_url    varchar(200)
);

/*==============================================================*/
/* Table: t_g_operate_logs                                      */
/*==============================================================*/
create table t_g_operate_logs 
(
   id                   numeric,
   user_name            varchar(30),
   project_code         varchar(30),
   profile              varchar(30),
   operate_code         int,
   execute_time         datetime,
   execute_result       int,
   result_info          text
);

/*==============================================================*/
/* Table: t_g_svn_command_logs                                  */
/*==============================================================*/
create table t_g_svn_command_logs 
(
   id                   numeric,
   user_name            varchar(30),
   repository_url       varchar(200),
   commands             varchar(300),
   create_time          datetime,
   real_name            varchar(30)
);

/*==============================================================*/
/* Table: t_g_svn_conflict                                      */
/*==============================================================*/
create table t_g_svn_conflict 
(
   id                   numeric,
   user_name            varchar(30),
   project_code         varchar(30),
   branch_url           varchar(200),
   trunk_url            varchar(200),
   file_name            varchar(200),
   status               int,
   result_info          text
);

/*==============================================================*/
/* Table: t_g_svn_change_logs                                   */
/*==============================================================*/
create table t_g_svn_change_logs 
(
   id                   numeric,
   user_name            varchar(30),
   type                 int,
   repository_url       varchar(200),
   file_name            varchar(300),
   create_time          datetime,
   result_status        int,
   result_info          text
);

/*==============================================================*/
/* Table: t_g_maven_command_logs                                */
/*==============================================================*/
create table t_g_maven_command_logs 
(
   id                   numeric,
   user_name            varchar(30),
   project_code         varchar(30),
   commands             text,
   create_time          datetime,
   result_info          text
);

