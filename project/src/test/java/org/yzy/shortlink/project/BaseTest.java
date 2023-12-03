package org.yzy.shortlink.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {
    public static final String TEST_URL = "create table t_link_goto_%\n" +
            "(\n" +
            "    id               bigint(20)                     not null auto_increment comment 'ID',\n" +
            "    gid            varchar(32)  default 'default' null comment '分组标识',\n" +
            "    full_short_url varchar(255) default null comment '完整短链接',\n" +
            "    primary key (id)\n" +
            ") ENGINE = InnoDB\n" +
            "  DEFAULT CHARSET = utf8mb4\n" +
            "    COMMENT = '短链接跳转表';";

    @Test
    public void creatTable() {
        for (int i = 0; i < 5; i++) {
            System.out.println(TEST_URL.replace("%", String.valueOf(i)));
        }
    }
}