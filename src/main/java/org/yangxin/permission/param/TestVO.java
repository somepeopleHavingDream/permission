package org.yangxin.permission.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yangxin
 * 2019/09/03 20:45
 */
@Getter
@Setter
public class TestVO {
    /**
     * 这个NotBlank的导入包和源码的不一样
     */
    @NotBlank
    private String msg;

    @NotNull
    private Integer id;
}
