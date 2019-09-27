package org.yangxin.permission.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.model.SysRole;
import org.yangxin.permission.param.AclParam;
import org.yangxin.permission.service.SysAclService;
import org.yangxin.permission.service.SysRoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 权限Controller
 *
 * @author yangxin
 * 2019/09/20 10:26
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {
    @Resource
    private SysAclService sysAclService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 新增权限
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAcl(AclParam param) {
        log.info("param: [{}]", param);

        sysAclService.save(param);
        return JsonData.success();
    }

    /**
     * 更新权限
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAcl(AclParam param) {
        log.info("param: [{}]", param);

        sysAclService.update(param);
        return JsonData.success();
    }

    @RequestMapping("page.json")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        log.info("aclModuleId: [{}]", aclModuleId);

        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }

    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId) {
        log.info("aclId: [{}]", aclId);

        // 获得该权限点所对应的全部角色记录
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        log.info("roleList.size: [{}]", roleList);

        Map<String, Object> map = Maps.newHashMap();
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));

        return JsonData.success(map);
    }
}
