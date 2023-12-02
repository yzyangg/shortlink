package org.yzy.shortlink.admin.toolkit;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.yzy.shortlink.admin.BaseTest;

@Slf4j
public class JWTUtilTest extends BaseTest {
    @Test
    public void testJWT() {
        String token = JWTUtil.generateToken("yzy");
        System.out.println(token);
        System.out.println(JWTUtil.getUsernameFromToken(token));
    }
}