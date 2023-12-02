package org.yzy.shortlink.admin.toolkit;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * @author yzy
 * @version 1.0
 * @description jwt工具类
 * @date 2023/12/1 1:22
 */

public class JWTUtil {

    /**
     * 签名
     */
    private static String SIGNATURE = "yzy";

    /**
     * 生成token
     *
     * @param map 传入payload
     * @return 返回token
     */
    public static String genToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 100000000);
        return builder.sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token
     *
     * @param token token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

    /**
     * 获取token中payload
     *
     * @param token token
     * @return payload
     */
    public static DecodedJWT parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }
}

