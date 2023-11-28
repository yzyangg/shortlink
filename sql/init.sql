CREATE TABLE `t_user`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `t_user_0`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_1`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_2`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_3`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_4`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_5`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_6`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_7`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_8`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `t_user_9`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',
    `password`      varchar(512) DEFAULT NULL COMMENT '密码',
    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',
    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',
    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



create table t_link_0
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_1
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_2
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_3
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_4
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_5
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_6
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_7
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_8
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
create table t_link_9
(
    id              bigint auto_increment comment 'ID'
        primary key,
    domain          varchar(128)  null comment '域名',
    short_uri       varchar(8)    null comment '短链接',
    full_short_url  varchar(128)  null comment '完整短链接',
    origin_url      varchar(1024) null comment '原始链接',
    click_num       int default 0 null comment '点击量',
    gid             varchar(32)   null comment '分组标识',
    enable_status   tinyint(1)    null comment '启用标识 0:未启用 1:已启用',
    create_type     tinyint(1)    null comment '创建类型 0:控制台 1:接口',
    valid_date_type tinyint(1)    null comment '有效期类型 0：永久有效 1：用户自定义',
    valid_date      datetime      null comment '有效期',
    `describe`      varchar(1024) null comment '描述',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '修改时间',
    del_flag        tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
    constraint idx_unique_full_short_uri
        unique (full_short_url)
);
