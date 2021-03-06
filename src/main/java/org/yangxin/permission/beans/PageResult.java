package org.yangxin.permission.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分页查询结构
 *
 * @author yangxin
 * 2019/09/16 10:30
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {
    private List<T> data;
    private int total;
}
