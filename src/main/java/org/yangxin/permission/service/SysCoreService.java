package org.yangxin.permission.service;

import org.springframework.stereotype.Service;
import org.yangxin.permission.dao.SysAclMapper;
import org.yangxin.permission.dao.SysRoleAclMapper;
import org.yangxin.permission.dao.SysRoleUserMapper;

import javax.annotation.Resource;

/**
 * @author yangxin
 * 2019/09/10 15:36
 */
@Service
public class SysCoreService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
//    @Resource
//    private SCService
}
