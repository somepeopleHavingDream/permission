package org.yangxin.permission.param;

import lombok.Data;

/**
 * 查询日志参数
 *
 * @author yangxin
 * 2019/09/30 16:43
 */
@Data
public class SearchLogParam {
    /**
     * LogType
     */
    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String fromTime;

    private String toTime;
}
