package org.yangxin.permission.util;

import java.util.Date;
import java.util.Random;

/**
 * 密码工具类
 *
 * @author yangxin
 * 2019/09/16 09:54
 */
public class PasswordUtil {
    private final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    private final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    /**
     * 随机密码
     */
    public static String randomPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random(new Date().getTime());
        boolean flag = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
            if (flag) {
                stringBuilder.append(num[random.nextInt(num.length)]);
            } else {
                stringBuilder.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return stringBuilder.toString();
    }
}
