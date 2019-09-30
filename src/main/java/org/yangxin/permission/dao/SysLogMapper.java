package org.yangxin.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.dto.SearchLogDto;
import org.yangxin.permission.model.SysLog;
import org.yangxin.permission.model.SysLogWithBLOBs;

import java.util.List;

public interface SysLogMapper {
    /**
     * 通过SearchDto对象，分页查询
     */
    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("dto") SearchLogDto dto, @Param("page")PageQuery page);

    /**
     * 通过SearchDto对象，统计记录总数
     */
    int countBySearchDto(@Param("dto")SearchLogDto dto);

    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);
}