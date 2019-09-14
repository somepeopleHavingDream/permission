package org.yangxin.permission.service;

import org.springframework.stereotype.Service;
import org.yangxin.permission.dao.SysUserMapper;

import javax.annotation.Resource;

/**
 * 用户Service类
 *
 * @author yangxin
 * 2019/09/15 12:13
 */
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

//    public void save(UserParam param) {
//        BeanValidator.check(param);
//        if ()
//    }
//
//    public boolean checkTelephoneExist(String telephone, Integer userId) {
//        return sysUserMapper.c
//    }
}
