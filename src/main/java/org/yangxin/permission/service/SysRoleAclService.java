package org.yangxin.permission.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import java.util.Set;

/**
 * 角色权限Service
 *
 * @author yangxin
 * 2019/09/25 10:43
 */
@Service
public class SysRoleAclService {
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
//    private SysLogMapper sysLogMapper;
    private SysLogService sysLogService;

    /**
     * 更改角色权限
     */
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList());
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
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
        sysRoleAclMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }

       List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl.builder()
                    .roleId(roleId)
                    .aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operatorTime(new Date())
                    .build();
        }
        sysRoleAclMapper.batchInsert(roleAclList);

    }

//    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
//        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
//        sysLog.setType(LogType.TYPE_ROLE_ACL);
//        sysLog.setTargetId(roleId);
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
//
//    }
}
