package org.yangxin.permission.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysRoleAclMapper;
import org.yangxin.permission.model.SysRoleAcl;
import org.yangxin.permission.util.IpUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 角色权限Service
 *
 * @author yangxin
 * 2019/09/25 10:43
 */
@Service
@Slf4j
public class SysRoleAclService {
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 更改角色权限
     */
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        // 获得该角色原来所拥有的全部权限Id
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        log.info("originAclIdList.size: [{}]", originAclIdList.size());

        // 目前这段代码看起来没啥作用，它也没有启动删除权限的作用
        if (Objects.equals(aclIdList.size(), originAclIdList.size())) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            log.info("originAclIdSet: [{}]", originAclIdSet);
            log.info("aclIdSet: [{}]", aclIdSet);

            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }

        updateRoleAcls(roleId, aclIdList);
        sysLogService.saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }

    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        // 删除角色权限表中以前的记录
        sysRoleAclMapper.deleteByRoleId(roleId);

        // 判空
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }

        // 构建角色权限集合
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder()
                    .roleId(roleId)
                    .aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operatorTime(new Date())
                    .build();
            roleAclList.add(roleAcl);
        }

        // 数据库操作，批量操作
        sysRoleAclMapper.batchInsert(roleAclList);
    }
}
