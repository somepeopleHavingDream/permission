package org.yangxin.permission.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Gson工具类
 *
 * @author yangxin
 * 2019/09/26 09:46
 */
public class GsonUtil {
    private static final Gson gson = new Gson();

    /**
     * Json字符串转对象
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        return gson.fromJson(str, clazz);
    }

    /**
     * Json字符串转数组
     */
    public static <T> List<T> str2List(String str, Class<T> clazz) {
        return gson.fromJson(str, new TypeToken<List<T>>(){}.getType());
    }

    /**
     * 对象转Json字符串
     */
    public static <T> String obj2String(T src) {
        return gson.toJson(src);
    }
}
