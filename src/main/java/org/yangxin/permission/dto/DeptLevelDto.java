package org.yangxin.permission.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.yangxin.permission.model.SysDept;

import java.util.List;

/**
 * 部门层级Dto
 *
 * @author yangxin
 * 2019/09/10 15:48
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    /**
     * 子部门集合；相比SysDept类，多了这个属性
     */
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * 将SysDept对象转换成DeptLevelDto对象
     */
    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
