package org.yangxin.permission.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 层级工具类
 *
 * @author yangxin
 * 2019/09/06 11:03
 */
public class LevelUtil {
    private final static String SEPARATOR = ".";
    public final static String ROOT = "0";

    /**
     * 计算出部门级别
     *
     * 0
     * 0.1
     * 0.1.2
     * 0.1.3
     * 0.4
     */
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
