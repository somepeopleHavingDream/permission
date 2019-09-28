package org.yangxin.permission.common;

import org.yangxin.permission.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * request封装类
 *
 * @author yangxin
 * 2019/09/05 17:36
 */
public class RequestHolder {
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    /**
     * 设置登录用户
     */
    public static void add(SysUser sysUser) {
        userHolder.set(sysUser);
    }

    /**
     * 设置请求
     */
    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    /**
     * 返回当前用户
     */
    public static SysUser getCurrentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
