package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.model.SysRoleUser;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    /**
     * 批量添加角色用户
     */
    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    /**
     * 根据角色Id，删除记录
     */
    void deleteByRoleId(@Param("roleId") int roleId);

    /**
     * 通过角色Id，获得用户Id记录集
     * @param roleId 角色Id
     */
    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    /**
     * 通过角色Id集，获取用户Id集
     */
    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    /**
     * 获得该用户所对应的所有角色
     */
    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);
}