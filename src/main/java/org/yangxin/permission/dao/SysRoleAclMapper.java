package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.model.SysRoleAcl;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    /**
     * 批量插入角色权限记录
     */
    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);

    /**
     * 通过角色Id删除角色记录
     *
     * @param roleId 角色Id
     */
    void deleteByRoleId(@Param("roleId") int roleId);

    /**
     * 通过权限Id，获得角色Id集
     *
     * @param aclId 权限Id
     */
    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);

    /**
     * 通过角色集合查询所有的权限集合
     */
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}