-- 部门表 --
drop table if exists sys_dept;
create table sys_dept (
    id int not null auto_increment comment '部门Id',
    name varchar(20) not null default '' comment '部门名称',
    parent_id int not null default 0 comment '上级部门Id',
    level varchar(200) not null default '' comment '部门层级',
    seq int not null default 0 comment '部门在当前层级下的顺序，由小到大',
    remark varchar(200) default '' comment '备注',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '部门表';

INSERT INTO permission.sys_dept (id, name, parent_id, level, seq, remark, operator, operate_time, operate_ip) VALUES (1, '技术部', 0, '0', 1, '', 'admin', '2019-10-01 02:21:54', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_dept (id, name, parent_id, level, seq, remark, operator, operate_time, operate_ip) VALUES (2, '后端开发', 1, '0.1', 1, '', 'admin', '2019-10-01 02:23:07', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_dept (id, name, parent_id, level, seq, remark, operator, operate_time, operate_ip) VALUES (3, '前端开发', 1, '0.1', 2, '', 'admin', '2019-10-01 02:23:18', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_dept (id, name, parent_id, level, seq, remark, operator, operate_time, operate_ip) VALUES (4, 'UI设计', 1, '0.1', 3, '', 'admin', '2019-10-01 02:23:32', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_dept (id, name, parent_id, level, seq, remark, operator, operate_time, operate_ip) VALUES (5, '产品部', 0, '0', 2, '', 'admin', '2019-10-01 02:23:47', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_dept (id, name, parent_id, level, seq, remark, operator, operate_time, operate_ip) VALUES (6, '客服部', 0, '0', 3, '客服部', 'admin', '2019-10-01 02:25:20', '0:0:0:0:0:0:0:1');

-- 用户表 --
drop table if exists sys_user;
create table sys_user (
    id int not null auto_increment comment '用户Id',
    username varchar(20) not null default '' comment '用户名称',
    telephone varchar(13) not null default '' comment '手机号',
    mail varchar(20) not null default '' comment '邮箱',
    password varchar(40) not null default '' comment '加密后的密码',
    dept_id int not null default 0 comment '用户所在部门的Id',
    status int not null default 1 comment '状态，1：正常，0：冻结状态，2：删除',
    remark varchar(200) default '' comment '备注',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '部门表';

INSERT INTO permission.sys_user (id, username, telephone, mail, password, dept_id, status, remark, operator, operate_time, operate_ip) VALUES (1, 'admin', '18774525398', 'admin@qq.com', '25D55AD283AA400AF464C76D713C07AD', 2, 1, '', '', '2019-10-01 15:02:12', '');
INSERT INTO permission.sys_user (id, username, telephone, mail, password, dept_id, status, remark, operator, operate_time, operate_ip) VALUES (2, 'jimin@qq.com', '18774525399', '1393003288@qq.com', '25D55AD283AA400AF464C76D713C07AD', 2, 1, '', '', '2019-10-01 15:27:26', '');
INSERT INTO permission.sys_user (id, username, telephone, mail, password, dept_id, status, remark, operator, operate_time, operate_ip) VALUES (3, 'rm', '18774525333', 'rm@qq.com', '25D55AD283AA400AF464C76D713C07AD', 2, 1, 'rm', 'admin', '2019-10-01 02:30:02', '0:0:0:0:0:0:0:1');

-- 权限模块表 --
drop table if exists sys_acl_module;
create table sys_acl_module (
    id int not null auto_increment comment '权限模块Id',
    name varchar(20) not null default '' comment '权限模块名称',
    parent_id int not null default 0 comment '上级权限模块Id',
    level varchar(200) not null default '' comment '权限模块层级',
    seq int not null default 0 comment '权限模块在当前层级下的顺序，由小到大',
    status int not null default 1 comment '状态，1：正常，0：冻结状态，2：删除',
    remark varchar(200) default '' comment '备注',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '权限模块表';

INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (1, '产品管理', 0, '0', 1, 1, '', 'admin', '2019-10-01 02:30:53', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (2, '订单管理', 0, '0', 2, 1, '', 'admin', '2019-10-01 02:31:18', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (3, '公告管理', 0, '0', 3, 1, '', 'admin', '2019-10-01 02:31:33', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (4, '权限管理', 0, '0', 4, 1, '', 'admin', '2019-10-01 02:31:45', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (5, '运维管理', 0, '0', 5, 1, '', 'admin', '2019-10-01 02:32:06', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (6, '权限管理', 4, '0.4', 1, 1, '', 'admin', '2019-10-01 02:32:41', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (7, '角色管理', 4, '0.4', 2, 1, '', 'admin', '2019-10-01 02:33:03', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (8, '用户管理', 4, '0.4', 3, 1, '', 'admin', '2019-10-01 02:33:14', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_acl_module (id, name, parent_id, level, seq, status, remark, operator, operate_time, operate_ip) VALUES (9, '权限更新记录管理', 4, '0.4', 4, 1, '', 'admin', '2019-10-01 02:33:33', '0:0:0:0:0:0:0:1');

-- 权限表 --
drop table if exists sys_acl;
create table sys_acl (
    id int not null auto_increment comment '权限id',
    code varchar(20) not null default '' comment '权限码',
    name varchar(20) not null default '' comment '权限名称',
    acl_module_id int not null default 0 comment '权限所在的权限模块id',
    url varchar(100) not null default '' comment '请求的url，可以填正则表达式',
    type int not null default 3 comment '类型，1：菜单，2：按钮，3：其他',
    seq int not null default 0 comment '权限模块在当前层级下的顺序，由小到大',
    status int not null default 1 comment '状态，1：正常，0：冻结状态，2：删除',
    remark varchar(200) default '' comment '备注',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '权限表';

INSERT INTO permission.sys_acl (id, code, name, acl_module_id, url, type, seq, status, remark, operator, operate_time, operate_ip) VALUES (1, '20191001153452_67', '进入权限更新管理页面', 9, '/sys/log/log.page', 1, 1, 1, '', 'admin', '2019-10-01 02:34:52', '0:0:0:0:0:0:0:1');

-- 角色表 --
drop table if exists sys_role;
create table sys_role (
    id int not null auto_increment comment '角色id',
    name varchar(20) not null,
    type int not null default 1 comment '角色的类型，1：管理员角色，2：其他',
    status int not null default 1 comment '状态，1：正常，0：冻结状态，2：删除',
    remark varchar(200) default '' comment '备注',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '角色表';

INSERT INTO permission.sys_role (id, name, type, status, remark, operator, operate_time, operate_ip) VALUES (1, '产品管理员', 1, 1, '', 'admin', '2019-10-01 02:35:55', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_role (id, name, type, status, remark, operator, operate_time, operate_ip) VALUES (2, '订单管理员', 1, 1, '', 'admin', '2019-10-01 02:36:21', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_role (id, name, type, status, remark, operator, operate_time, operate_ip) VALUES (3, '公告管理员', 1, 1, '', 'admin', '2019-10-01 02:36:29', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_role (id, name, type, status, remark, operator, operate_time, operate_ip) VALUES (4, '权限管理员', 1, 1, '', 'admin', '2019-10-01 02:36:38', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_role (id, name, type, status, remark, operator, operate_time, operate_ip) VALUES (5, '运维管理员', 1, 1, '运维', 'admin', '2019-10-01 02:36:59', '0:0:0:0:0:0:0:1');

-- 用户角色表 --
drop table if exists sys_role_user;
create table sys_role_user (
    id int not null auto_increment,
    role_id int not null comment '角色Id',
    user_id int not null comment '用户Id',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '用户角色表 ';

-- 角色权限表 --
drop table if exists sys_role_acl;
create table sys_role_acl (
    id int not null auto_increment,
    role_id int not null comment '角色Id',
    acl_id int not null comment '权限Id',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '角色权限表';

-- 日志表 --
drop table if exists sys_log;
create table sys_log (
    id int not null auto_increment,
    type int not null default 0 comment '权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系',
    target_id int not null comment '基于type后指定的对象id，比如用户、权限、角色等表的主键',
    old_value text comment '旧值',
    new_value text comment '新值',
    status int not null default 0 comment '当前是否复原过，0：没有，1：复原过',
    operator varchar(20) not null default '' comment '操作者',
    operate_time datetime not null default now() comment '最后一次操作时间',
    operate_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '日志表';

INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (1, 1, 1, '', '{"id":1,"name":"技术部","parentId":0,"level":"0","seq":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:21:54 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:21:54', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (2, 1, 2, '', '{"id":2,"name":"后端开发","parentId":1,"level":"0.1","seq":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:23:07 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:23:07', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (3, 1, 3, '', '{"id":3,"name":"前端开发","parentId":1,"level":"0.1","seq":2,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:23:18 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:23:18', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (4, 1, 4, '', '{"id":4,"name":"UI设计","parentId":1,"level":"0.1","seq":3,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:23:32 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:23:32', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (5, 1, 5, '', '{"id":5,"name":"产品部","parentId":0,"level":"0","seq":2,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:23:47 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:23:47', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (6, 1, 6, '', '{"id":6,"name":"客服部","parentId":0,"level":"0","seq":3,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:24:05 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:24:05', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (7, 1, 6, '{"id":6,"name":"客服部","parentId":0,"level":"0","seq":3,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:24:05 PM","operateIp":"0:0:0:0:0:0:0:1"}', '{"id":6,"name":"客服部","parentId":0,"level":"0","seq":3,"remark":"客服部","operator":"admin","operateTime":"Oct 1, 2019 3:25:20 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:25:20', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (8, 2, 3, '', '{"id":3,"username":"rm","telephone":"18774525333","mail":"rm@qq.com","password":"25D55AD283AA400AF464C76D713C07AD","deptId":2,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:29:40 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:29:40', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (9, 2, 3, '{"id":3,"username":"rm","telephone":"18774525333","mail":"rm@qq.com","password":"25D55AD283AA400AF464C76D713C07AD","deptId":2,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:29:40 PM","operateIp":"0:0:0:0:0:0:0:1"}', '{"id":3,"username":"rm","telephone":"18774525333","mail":"rm@qq.com","deptId":2,"status":1,"remark":"rm","operator":"admin","operateTime":"Oct 1, 2019 3:30:02 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:30:02', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (10, 3, 1, '', '{"id":1,"name":"产品管理","parentId":0,"level":"0","seq":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:30:53 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:30:54', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (11, 3, 2, '', '{"id":2,"name":"订单管理","parentId":0,"level":"0","seq":2,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:31:18 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:31:18', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (12, 3, 3, '', '{"id":3,"name":"公告管理","parentId":0,"level":"0","seq":3,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:31:33 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:31:33', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (13, 3, 4, '', '{"id":4,"name":"权限管理","parentId":0,"level":"0","seq":4,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:31:45 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:31:45', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (14, 3, 5, '', '{"id":5,"name":"运维管理","parentId":0,"level":"0","seq":5,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:32:06 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:32:06', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (15, 3, 6, '', '{"id":6,"name":"权限管理","parentId":4,"level":"0.4","seq":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:32:41 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:32:41', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (16, 3, 7, '', '{"id":7,"name":"角色管理","parentId":4,"level":"0.4","seq":2,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:33:03 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:33:03', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (17, 3, 8, '', '{"id":8,"name":"用户管理","parentId":4,"level":"0.4","seq":3,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:33:14 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:33:15', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (18, 3, 9, '', '{"id":9,"name":"权限更新记录管理","parentId":4,"level":"0.4","seq":4,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:33:33 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:33:33', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (19, 4, 1, '', '{"id":1,"code":"20191001153452_67","name":"进入权限更新管理页面","aclModuleId":9,"url":"/sys/log/log.page","type":1,"seq":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:34:52 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:34:52', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (20, 5, 1, '', '{"id":1,"name":"产品管理员","type":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:35:55 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:35:55', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (21, 5, 2, '', '{"id":2,"name":"订单管理员","type":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:36:21 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:36:21', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (22, 5, 3, '', '{"id":3,"name":"公告管理员","type":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:36:29 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:36:29', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (23, 5, 4, '', '{"id":4,"name":"权限管理员","type":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:36:38 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:36:39', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (24, 5, 5, '', '{"id":5,"name":"运维管理员","type":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:36:51 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:36:51', '0:0:0:0:0:0:0:1');
INSERT INTO permission.sys_log (id, type, target_id, old_value, new_value, status, operator, operate_time, operate_ip) VALUES (25, 5, 5, '{"id":5,"name":"运维管理员","type":1,"status":1,"remark":"","operator":"admin","operateTime":"Oct 1, 2019 3:36:51 PM","operateIp":"0:0:0:0:0:0:0:1"}', '{"id":5,"name":"运维管理员","type":1,"status":1,"remark":"运维","operator":"admin","operateTime":"Oct 1, 2019 3:36:59 PM","operateIp":"0:0:0:0:0:0:0:1"}', 1, 'admin', '2019-10-01 02:37:00', '0:0:0:0:0:0:0:1');