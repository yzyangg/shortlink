package org.yzy.shortlink.project.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HashUtilTest {

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            String s = HashUtil.generateShortLink("https://www.baidu.com");
            System.out.println(s);
        }


    }
}