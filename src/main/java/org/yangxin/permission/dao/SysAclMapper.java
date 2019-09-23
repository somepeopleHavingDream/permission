package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
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
}