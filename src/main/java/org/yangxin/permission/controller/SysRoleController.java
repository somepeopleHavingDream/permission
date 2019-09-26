package org.yangxin.permission.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.dto.AclModuleLevelDto;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.param.RoleParam;
import org.yangxin.permission.service.*;
import org.yangxin.permission.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色Controller
 *
 * @author yangxin
 * 2019/09/25 10:42
 */
@Controller
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleAclService sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService sysUserService;

    /**
     * 进入页面
     */
    @RequestMapping("role.page")
    public ModelAndView page() {
        log.info("进入角色管理页面");
        return new ModelAndView("role");
    }

    /**
     * 新增角色
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveRole(RoleParam param) {
        log.info("param: [{}]", param);

        sysRoleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新角色
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateRole(RoleParam param) {
        log.info("param: [{}]", param);

        sysRoleService.update(param);
        return JsonData.success();
    }

    /**
     * 获得列表
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }

    /**
     * 角色树
     *
     * @param roleId 角色Id
     */
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        log.info("roleId: [{}]", roleId);

        // 得到该角色所对应所有权限模型，权限模块是分级的，类似于一个树形结构，并且每个权限模型附带了所有的权限记录
        List<AclModuleLevelDto> aclModuleLevelDtoList = sysTreeService.roleTree(roleId);
        Preconditions.checkNotNull(aclModuleLevelDtoList, "aclModuleLevelDtoList为null");

        log.info("aclModuleLevelDtoList.size: [{}]", aclModuleLevelDtoList.size());
        return JsonData.success(aclModuleLevelDtoList);
    }

    /**
     * 更改权限
     *
     * @param roleId 角色Id
     * @param aclIds 权限Id集
     */
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        log.info("roleId: [{}], aclIds: [{}]", roleId, aclIds);

        List<Integer> aclIdList = StringUtil.splitToIntList(aclIds);
//        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }

    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId,
                                @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        log.info("roleId: [{}], userIds: [{}]", roleId, userIds);


        List<Integer> userIdList = StringUtil.splitToIntList(userIds);
//        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return JsonData.success();
    }

    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId) {
        log.info("roleId: [{}]", roleId);

        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.getAll();
        List<SysUser> unselectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserIdSet = selectedUserList.stream()
                .map(SysUser::getId)
                .collect(Collectors.toSet());
        for (SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }

        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return JsonData.success(map);
    }
}
