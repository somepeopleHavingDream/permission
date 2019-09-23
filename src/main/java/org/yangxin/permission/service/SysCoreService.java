package org.yangxin.permission.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.CacheKeyConstants;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysAclMapper;
import org.yangxin.permission.dao.SysRoleAclMapper;
import org.yangxin.permission.dao.SysRoleUserMapper;
import org.yangxin.permission.model.SysAcl;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.util.JsonMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangxin
 * 2019/09/10 15:36
 */
@Service
public class SysCoreService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysCacheService sysCacheService;

    /**
     * 获得角色权限
     *
     * @param roleId 角色Id
     */
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    /**
     * 从缓存中获得当前用户权限列表
     */
    public List<SysAcl> getCurrentUserAclListFromCache() {
        Integer userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList),
                        600, CacheKeyConstants.USER_ACLS,
                        String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }

    /**
     * 获得当前用户权限列表
     */
    List<SysAcl> getCurrentUserAclList() {
        Integer userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    /**
     * 拿到该用户的所有权限
     *
     * @param userId 用户Id
     */
    List<SysAcl> getUserAclList(int userId) {
        // 超级用户
        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        }

        // 获得该用户对应的所有角色
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        // 获得该用户对应的所有权限
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }

        // 返回所有权限
        return sysAclMapper.getByIdList(userAclIdList);
    }

    /**
     * 是否为超级管理员
     */
    private boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getCurrentUser();
        return sysUser.getMail().contains("admin");
    }
}
