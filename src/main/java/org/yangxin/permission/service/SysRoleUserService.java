package org.yangxin.permission.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysRoleUserMapper;
import org.yangxin.permission.dao.SysUserMapper;
import org.yangxin.permission.model.SysRoleUser;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.util.IpUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 角色用户Service
 *
 * @author yangxin
 * 2019/09/25 10:44
 */
@Service
@Slf4j
public class SysRoleUserService {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 获得有该角色的所有用户记录
     *
     * @param roleId 角色Id
     */
    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 更改角色用户
     *
     * @param roleId 角色Id
     * @param userIdList 用户Id集合
     */
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        // 获得该角色原来所拥有的全部用户记录
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        log.info("originUserIdList.size: [{}]", originUserIdList.size());

        // 我没搞懂这段代码用来干嘛的，但这段代码好像一直不会执行
        if (Objects.equals(originUserIdList.size(), userIdList.size())) {
//        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            log.info("originUserIdSet: [{}]", originUserIdSet);
            log.info("userIdSet: [{}]", userIdSet);

            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }

        // 更新角色用户
        updateRoleUsers(roleId, userIdList);
        sysLogService.saveRoleUserLog(roleId, originUserIdList, userIdList);
    }

    /**
     * 更新角色用户
     *
     * @param roleId 角色Id
     * @param userIdList 用户Id集合
     */
    @Transactional
    void updateRoleUsers(int roleId, List<Integer> userIdList) {
        // 删除原来的角色用户记录
        sysRoleUserMapper.deleteByRoleId(roleId);

        // 判空
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        // 构建角色用户集合
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operatorTime(new Date())
                    .build();
            roleUserList.add(roleUser);
        }

        // 数据库操作，批量插入
        sysRoleUserMapper.batchInsert(roleUserList);
    }
}
