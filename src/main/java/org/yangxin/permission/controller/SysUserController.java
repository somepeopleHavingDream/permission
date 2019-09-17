package org.yangxin.permission.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.beans.PageResult;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.param.UserParam;
import org.yangxin.permission.service.SysRoleService;
import org.yangxin.permission.service.SysTreeService;
import org.yangxin.permission.service.SysUserService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author yangxin
 * 2019/09/14 12:11
 */
@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 未授权页面
     */
    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }

    /**
     * 新增
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam param) {
        log.info("param: [{}]", param);

        sysUserService.save(param);
        return JsonData.success();
    }

    /**
     * 更新
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam param) {
        sysUserService.update(param);
        return JsonData.success();
    }

    /**
     * 分页查
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery) {
        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
