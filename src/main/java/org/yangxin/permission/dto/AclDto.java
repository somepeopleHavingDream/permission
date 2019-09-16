package org.yangxin.permission.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.yangxin.permission.model.SysAcl;

/**
 * 权限dto
 *
 * @author yangxin
 * 2019/09/16 10:49
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {
    /**
     * 是否要默认选中
     */
    private boolean checked = false;

    /**
     * 是否有权限操作
     */
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
