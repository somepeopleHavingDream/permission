package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.model.SysUser;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 批量获得用户记录
     */
    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    /**
     * 得到全部的用户记录
     */
    List<SysUser> getAll();

    /**
     * 查看有多少条与给定部门Id相同的记录数
     *
     * @param deptId 部门Id
     */
    int countByDeptId(@Param("deptId") int deptId);

    /**
     * 查看有多少条与给定电话号码相同的记录数
     *
     * @param telephone 电话号码
     * @param id 用户id
     */
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    /**
     * 查看有多少条与给定邮箱地址相同的记录数
     *
     * @param mail 邮箱地址
     * @param id 用户id
     */
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    /**
     * 根据部门Id，分页查询
     *
     * @param deptId  部门id
     * @param page 分页查询对象
     */
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page")PageQuery page);

    /**
     * 通过关键字查看用户记录
     */
    SysUser findByKeyword(@Param("keyword") String keyword);
}