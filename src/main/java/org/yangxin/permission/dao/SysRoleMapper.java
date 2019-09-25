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

    /**
     * 得到所有角色记录
     */
    List<SysRole> getAll();

    /**
     * 统计记录总数
     */
    int countByName(@Param("name") String name, @Param("id") Integer id);

    /**
     * 批量获得角色记录集
     *
     * @param idList 角色Id集合
     */
    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);
}