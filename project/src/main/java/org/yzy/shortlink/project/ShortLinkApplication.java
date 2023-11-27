package org.yzy.shortlink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/27 23:34
 */
@SpringBootApplication
@MapperScan("org.yzy.shortlink.project.dao.mapper")
public class ShortLinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortLinkApplication.class, args);
    }
}
