package org.yangxin.permission.service;

import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.LogType;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysLogMapper;
import org.yangxin.permission.model.*;
import org.yangxin.permission.util.GsonUtil;
import org.yangxin.permission.util.IpUtil;
import org.yangxin.permission.util.JsonMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 日志Service
 *
 * @author yangxin
 * 2019/09/06 11:57
 */
@Service
public class SysLogService {
    @Resource
    private SysLogMapper sysLogMapper;

    void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 存储角色表日志
     */
    void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : GsonUtil.obj2String(before));
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : GsonUtil.obj2String(after));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 存储权限模块表日志
     */
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 存储部门日志
     *
     * @param before 上一次的记录
     * @param after 这一次的记录
     */
    public void saveDeptLog(SysDept before, SysDept after) {
        // 设置SysLogWithBLOS对象
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
//        setSysLogField(before, after, sysLog);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        // todo 做到这里要开始重构obj2String方法了
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 存储用户日志
     */
    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 保存权限点日志
     */
    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperationAndStatus(sysLog);

        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 操作者、Ip、操作时间、操作状态
     */
    private void setOperationAndStatus(SysLogWithBLOBs sysLog) {
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperatorTime(new Date());
        sysLog.setStatus(1);
    }
}
