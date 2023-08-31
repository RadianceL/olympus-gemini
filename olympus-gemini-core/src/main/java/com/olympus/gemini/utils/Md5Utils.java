package com.olympus.gemini.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具
 * since 2020/2/28
 *
 * @author eddie
 */
public class Md5Utils {

    private final static String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (byte index : b) {
            builder.append(byteToHexString(index));
        }
        return builder.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    public static String encode(String origin) throws NoSuchAlgorithmException {
        String resultString = origin;
        MessageDigest md = MessageDigest.getInstance("MD5");
        resultString = byteArrayToHexString(md.digest(resultString.getBytes(StandardCharsets.UTF_8)));
        return resultString;
    }
}
