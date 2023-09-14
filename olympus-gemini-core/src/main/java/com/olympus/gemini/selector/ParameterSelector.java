package com.olympus.gemini.selector;

import com.olympus.gemini.utils.Md5Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

/**
 * 参数选择器
 *
 * @author eddie.lys
 * @since 2023/8/28
 */
public class ParameterSelector {


    /**
     * 从参数中获取唯一值
     */
    public static String getUniqueFromParameter(String identificationCode, Object[] args) {
        if (args == null || args.length == 0) {
            return identificationCode;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(identificationCode).append("-");
        for (Object arg : args) {
            if (isPrimitiveOrWrapper(arg)) {
                builder.append(arg);
                continue;
            }
            Field[] methods = arg.getClass().getDeclaredFields();
            for (Field field : methods) {
                try {
                    Object o = field.get(arg);
                    builder.append(o.hashCode());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        String combinedString = builder.toString();
        try {
            return Md5Utils.encode(combinedString);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPrimitiveOrWrapper(Object obj) {
        return obj instanceof Byte ||
                obj instanceof Short ||
                obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Double ||
                obj instanceof Character ||
                obj instanceof Boolean ||
                obj instanceof String;  // Include String as well
    }
}
