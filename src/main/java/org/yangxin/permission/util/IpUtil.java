package org.yangxin.permission.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ip工具类
 *
 * @author yangxin
 * 2019/09/06 11:20
 */
@Slf4j
public class IpUtil {
    private final static String ERROR_IP = "127.0.0.1";

    public final static Pattern pattern = Pattern
            .compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");

    /**
     * 取外网IP
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        // 过滤反向代理的ip
        String[] split = ip.split(",");
        if (split.length >= 1) {
            // 得到第一个ip，即客户端真实Ip
            ip = split[0];
        }

        ip = ip.trim();
        if (ip.length() > 23) {
            ip = ip.substring(0, 23);
        }

        return ip;
    }

    /**
     * 获取用户的真实IP
     */
    private static String getUserIP(HttpServletRequest request) {
        // 优先取X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = ERROR_IP;
            }
        }

        if ("unknown".equalsIgnoreCase(ip)) {
            ip = ERROR_IP;
            return ip;
        }

        int pos = ip.indexOf(",");
        if (pos >= 0) {
            ip = ip.substring(0, pos);
        }

        return ip;
    }

    /**
     * 得到最后的Ip段
     */
    public static String getLastIpSegment(HttpServletRequest request) {
        String ip = getUserIP(request);
        return ip != null ? ip.substring(ip.lastIndexOf(".") + 1) : "0";
    }

    /**
     * 判断我们获取的ip是否是一个符合规则ip
     *
     * @param ip ip
     */
    public static boolean isValidIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            log.debug("ip is null. valid result is false");
            return false;
        }

        Matcher matcher = pattern.matcher(ip);
        boolean isValid = matcher.matches();
        log.debug("valid ip: " + ip + "result is: " + isValid);
        return isValid;
    }

    public static String getLastServerIpSegment() {
        String ip = getServerIP();
        return ip != null ? ip.substring(ip.lastIndexOf('.') + 1) : "0";
    }

    private static String getServerIP() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}
