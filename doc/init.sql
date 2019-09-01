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
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '部门表';

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
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '部门表';

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
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '权限模块表';

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
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '权限表';

-- 角色表 --
drop table if exists sys_role;
create table sys_role (
    id int not null auto_increment comment '角色id',
    name varchar(20) not null,
    type int not null default 1 comment '角色的类型，1：管理员角色，2：其他',
    status int not null default 1 comment '状态，1：正常，0：冻结状态，2：删除',
    remark varchar(200) default '' comment '备注',
    operator varchar(20) not null default '' comment '操作者',
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '角色表';

-- 用户角色表 --
drop table if exists sys_role_user;
create table sys_role_user (
    id int not null auto_increment,
    role_id int not null comment '角色Id',
    user_id int not null comment '用户Id',
    operator varchar(20) not null default '' comment '操作者',
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '用户角色表 ';

-- 角色权限表 --
drop table if exists sys_role_acl;
create table sys_role_acl (
    id int not null auto_increment,
    role_id int not null comment '角色Id',
    acl_id int not null comment '权限Id',
    operator varchar(20) not null default '' comment '操作者',
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
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
    operator_time datetime not null default now() comment '最后一次操作时间',
    operator_ip varchar(20) not null default '' comment '最后一次更新操作者的ip地址',
    primary key (id)
) charset = utf8 comment '日志表';