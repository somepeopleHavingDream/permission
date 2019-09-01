package org.yangxin.permission.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Json封装类
 *
 * @author yangxin
 * 2019/09/01 11:57
 */
@Getter
@Setter
public class JsonData {
    private boolean ret;
    private String msg;
    private Object data;

    private JsonData(boolean ret) {
        this.ret = ret;
    }

    public static JsonData success(Object object, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;

        return jsonData;
    }

    public static JsonData success(Object object) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;

        return jsonData;
    }

    public static JsonData success() {
        return new JsonData(true);
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;

        return jsonData;
    }
}
