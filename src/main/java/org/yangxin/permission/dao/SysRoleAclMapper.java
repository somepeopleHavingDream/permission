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
     * 通过角色集合查询所有的权限集合
     */
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}