package org.yangxin.permission.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

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

    @NotNull(message = "id不可以为空")
    @Max(value = 10, message = "id 不能大于10")
    @Min(value = 0, message = "id 至少大于等于0")
    @NotNull
    private Integer id;

    @NotEmpty
    private List<String> str;
}
