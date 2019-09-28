package org.yangxin.permission.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 *
 * @author yangxin
 * 2019/09/25 11:40
 */
public class StringUtil {
//    public static List<Integer> splitToListInt(String str) {
//        List<String> strList = Splitter.on(",")
//                .trimResults()
//                .omitEmptyStrings()
//                .splitToList(str);
//
//        return strList.stream()
//                .map(Integer::parseInt)
//                .collect(Collectors.toList());
//    }

    public static List<Integer> splitToIntList(String str) {
        if (str == null) {
            return Collections.emptyList();
        } else {
            List<String> strList = Arrays.asList(str.split(","));
            return strList.stream()
                    .filter(e -> !("").equals(e))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
    }
}
