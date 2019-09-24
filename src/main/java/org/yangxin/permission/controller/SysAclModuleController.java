package org.yangxin.permission.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.param.AclModuleParam;
import org.yangxin.permission.service.SysAclModuleService;
import org.yangxin.permission.service.SysTreeService;

import javax.annotation.Resource;

/**
 * 权限模块Controller
 *
 * @author yangxin
 * 2019/09/20 10:14
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {
    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 进入权限模块页面
     */
    @RequestMapping("/acl.page")
    public ModelAndView page() {
        log.info("进入权限模块管理页面");
        return new ModelAndView("acl");
    }

    /**
     * 新增权限模块
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam param) {
        log.info("param: [{}]", param);

        sysAclModuleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新权限模块
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param) {
        log.info("param: [{}]", param);

        sysAclModuleService.update(param);
        return JsonData.success();
    }

    /**
     * 权限模块树
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        return JsonData.success(sysTreeService.aclModuleTree());
    }

    /**
     * 删除权限模块
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int id) {
        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
