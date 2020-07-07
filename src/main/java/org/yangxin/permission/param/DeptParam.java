package org.yangxin.permission.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 部门参数
 *
 * @author yangxin
 * 2019/09/06 10:33
 */
@Data
public class DeptParam {

    /**
     * id
     */
    private Integer id;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不可以为空")
    @Length(max = 15, min = 2, message = "部门名称长度需要在2-15个字之间")
    private String name;

    /**
     * 父级部门Id，默认为0
     */
    private Integer parentId = 0;

    /**
     * 展示顺序
     */
    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;

    /**
     * 备注
     */
    @Length(max = 150, message = "备注的长度需要在150个字以内")
    private String remark;
}
