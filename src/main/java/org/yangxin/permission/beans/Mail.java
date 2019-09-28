package org.yangxin.permission.beans;

import lombok.Data;

import java.util.Set;

/**
 * 邮件类
 *
 * @author yangxin
 * 2019/09/20 09:50
 */
@Data
public class Mail {
    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件正文
     */
    private String message;

    /**
     * 收件者地址
     */
    private Set<String> receivers;
}
