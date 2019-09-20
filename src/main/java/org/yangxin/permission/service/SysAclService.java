package org.yangxin.permission.service;

import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.param.AclParam;

/**
 * 权限Service
 *
 * @author yangxin
 * 2019/09/20 10:27
 */
@Service
public class SysAclService {
    /**
     * 新增权限
     */
    public void save(AclParam param) {
    }

    /**
     * 更新权限
     */
    public void update(AclParam param) {
    }

    /**
     * 通过权限模块Id，分页查询数据
     */
    public Object getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery) {
        return null;
    }
}
