package org.yzy.shortlink.project.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author yzy
 * @version 1.0
 * @description hash工具类
 * @date 2023/11/27 8:47
 */
public class HashUtil {

    public static String generateShortLink(String longUrl) {
        // 使用SHA-256作为哈希函数
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longUrl.getBytes());

            // 使用Base64进行编码
            String base64encoded = Base64.getEncoder().encodeToString(hash);

            // 剔除Base64中的非Base62字符，只留下数字和字母部分
            String base62 = base64encoded.replaceAll("[^a-zA-Z0-9]", "");

            // 获取前8个字符作为短链接
            return base62.substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
