package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.model.SysRole;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);
}