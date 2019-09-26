package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.model.SysAcl;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    /**
     * 根据url获得权限记录
     */
    List<SysAcl> getByUrl(@Param("url") String url);

    /**
     * 通过权限模型Id，分页查询权限记录
     *
     * @param aclModuleId 权限模型Id
     * @param page 分页对象
     */
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page")PageQuery page);

    /**
     * 统计特定权限模块下有多少条权限记录
     *
     * @param aclModuleId 权限模块Id
     */
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    /**
     * 得到全部权限
     */
    List<SysAcl> getAll();

    /**
     * 通过id，获得所有权限记录
     */
    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);

    /**
     * 根据权限模型Id和权限点名称，统计记录数
     *
     * @param aclModuleId 权限模型Id
     * @param name 权限点名称
     * @param id 权限点Id
     */
    int countByNameAndAclModuleId(int aclModuleId, String name, Integer id);
}