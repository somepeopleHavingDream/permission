package org.yangxin.permission.service;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.yangxin.permission.dao.SysRoleMapper;
import org.yangxin.permission.dao.SysRoleUserMapper;
import org.yangxin.permission.model.SysRole;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色Service
 *
 * @author yangxin
 * 2019/09/14 12:15
 */
@Service
public class SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList =sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 通过权限Id，获得角色列表
     *
     * @param aclId 权限Id
     */
    public List<SysRole> getRoleListByAclId(int aclId) {
        return null;
    }

    /**
     * 通过角色集合，获取用户集合
     */
    public Object getUserListByRoleList(List<SysRole> roleList) {
        return null;
    }
}
