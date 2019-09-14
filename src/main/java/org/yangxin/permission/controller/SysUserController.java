package org.yangxin.permission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yangxin.permission.service.SysRoleService;
import org.yangxin.permission.service.SysTreeService;
import org.yangxin.permission.service.SysUserService;

import javax.annotation.Resource;

/**
 * 用户Controller
 *
 * @author yangxin
 * 2019/09/14 12:11
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }

//    @RequestMapping("/save.json")
//    @ResponseBody
//    public JsonData saveUser(UserParam param) {
//        sysUserService
//    }
}
