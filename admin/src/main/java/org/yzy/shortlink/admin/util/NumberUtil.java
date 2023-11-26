package org.yzy.shortlink.admin.util;

import java.util.UUID;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/26 0:04
 */
public class NumberUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "");
        return uuid.substring(0, 6); // 截取前六位字符作为结果
    }

    public static void main(String[] args) {
        String randomString = generateRandomString();
        System.out.println("Generated Random String: " + randomString);
    }
}
