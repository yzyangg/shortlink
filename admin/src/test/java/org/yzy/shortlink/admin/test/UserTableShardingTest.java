package org.yzy.shortlink.admin.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/21 0:47
 */
@SpringBootTest
public class UserTableShardingTest {
    public static final String CREAT_TABLE = "CREATE TABLE `t_user_%`\n" +
            "(\n" +
            "    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "    `username`      varchar(256) DEFAULT NULL COMMENT '用户名',\n" +
            "    `password`      varchar(512) DEFAULT NULL COMMENT '密码',\n" +
            "    `real_name`     varchar(256) DEFAULT NULL COMMENT '真实姓名',\n" +
            "    `phone`         varchar(128) DEFAULT NULL COMMENT '手机号',\n" +
            "    `mail`          varchar(512) DEFAULT NULL COMMENT '邮箱',\n" +
            "    `deletion_time` bigint(20)   DEFAULT NULL COMMENT '注销时间戳',\n" +
            "    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',\n" +
            "    `update_time`   datetime     DEFAULT NULL COMMENT '修改时间',\n" +
            "    `del_flag`      tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',\n" +
            "    PRIMARY KEY (`id`)\n" +
            ") ENGINE = InnoDB\n" +
            "  DEFAULT CHARSET = utf8mb4;";

    public static final String CREATE_GROUP = "" +
            "-- auto-generated definition\n" +
            "create table t_group_%\n" +
            "(\n" +
            "    id          bigint auto_increment comment 'ID'\n" +
            "        primary key,\n" +
            "    gid         varchar(32)  null comment '分组标识',\n" +
            "    name        varchar(64)  null comment '分组名称',\n" +
            "    username    varchar(256) null comment '创建分组用户名',\n" +
            "    sort_order  int          null comment '分组排序',\n" +
            "    create_time datetime     null comment '创建时间',\n" +
            "    update_time datetime     null comment '修改时间',\n" +
            "    del_flag    tinyint(1)   null comment '删除标识 0：未删除 1：已删除',\n" +
            "    constraint idx_unique_username_gid\n" +
            "        unique (gid, username)\n" +
            ");\n" +
            "\n";

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(CREAT_TABLE.replace("%", String.valueOf(i)));
        }
    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            System.out.println(CREATE_GROUP.replace("%", String.valueOf(i)));
        }
    }
}
