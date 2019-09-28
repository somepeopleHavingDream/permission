package org.yangxin.permission.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.yangxin.permission.model.SysAclModule;

import java.util.List;

/**
 * 权限模型级别Dto
 *
 * @author yangxin
 * 2019/09/16 10:47
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();
    private List<AclDto> aclList = Lists.newArrayList();

    /**
     * 将权限模块对象转换成AclModuleLevelDto对象
     */
    public static AclModuleLevelDto adapt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}
