package org.yangxin.permission.dto;

import lombok.Data;

import java.util.Date;

/**
 * 查询日志Dto
 *
 * @author yangxin
 * 2019/09/30 16:45
 */
@Data
public class SearchLogDto {
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
    private Date fromTime;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Date toTime;
}
