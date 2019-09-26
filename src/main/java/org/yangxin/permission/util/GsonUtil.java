package org.yangxin.permission.util;

import com.google.gson.Gson;

/**
 * Gson工具类
 *
 * @author yangxin
 * 2019/09/26 09:46
 */
public class GsonUtil {
    private static final Gson gson = new Gson();

    /**
     * 对象转Json字符串
     */
    public static <T> String obj2String(T src) {
        return src == null ? null : gson.toJson(src);
    }
}
