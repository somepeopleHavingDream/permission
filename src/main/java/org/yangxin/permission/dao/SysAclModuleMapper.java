package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.model.SysAclModule;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    /**
     * 统计记录数
     *
     * @param aclModuleId 权限模块Id
     */
    int countByParentId(@Param("aclModuleId") int aclModuleId);

    /**
     * 批量更新层级
     */
    void batchUpdateLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModuleList);

    /**
     * 得到所有权限模型
     */
    List<SysAclModule> getAllAclModule();

    /**
     * 通过权限模块名称和parentId查询记录条数
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    /**
     * 通过层级获得模块列表
     *
     * @param level 层级
     */
    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);
}