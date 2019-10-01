package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.model.SysDept;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    int countByParentId(@Param("deptId") int deptId);

    List<SysDept> getAllDept();
}