package org.yangxin.permission.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysRoleAclMapper;
import org.yangxin.permission.dao.SysRoleMapper;
import org.yangxin.permission.dao.SysRoleUserMapper;
import org.yangxin.permission.dao.SysUserMapper;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.model.SysRole;
import org.yangxin.permission.param.RoleParam;
import org.yangxin.permission.util.BeanValidator;
import org.yangxin.permission.util.IpUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色Service
 *
 * @author yangxin
 * 2019/09/14 12:15
 */
@Service
public class SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增角色
     */
    public void save(RoleParam param) {
        // 校验
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        // 新建角色对象
        SysRole role = SysRole.builder()
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        setOperation(role);

        // 数据库操作
        sysRoleMapper.insertSelective(role);
        sysLogService.saveRoleLog(null, role);
    }

    /**
     * 更新角色
     */
    public void update(RoleParam param) {
        // 检验
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        // 更新前
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        // 更新后
        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        setOperation(after);

        // 数据库操作
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);
    }

    /**
     * 得到所有角色记录
     */
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    /**
     * 获取该用户拥有的所有角色
     *
     * @param userId 用户Id
     */
    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList =sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 通过权限Id，获得角色列表
     *
     * @param aclId 权限Id
     */
    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        return CollectionUtils.isEmpty(roleIdList) ? Lists.newArrayList() : sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 通过角色集合，获取用户集合
     */
    public Object getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }

        // 获取角色Id集合
        List<Integer> roleIdList = roleList.stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);

        // 返回用户集合
        return CollectionUtils.isEmpty(userIdList) ? Lists.newArrayList() : sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 操作人、操作人Ip、操作时间
     */
    private void setOperation(SysRole role) {
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperatorTime(new Date());
    }

    /**
     * 检查该角色是否已存在
     *
     * @param name 角色名称
     * @param id 角色Id
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }
}
