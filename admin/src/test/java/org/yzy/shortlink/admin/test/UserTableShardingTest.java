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

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(CREAT_TABLE.replace("%", String.valueOf(i)));
        }
    }
}
