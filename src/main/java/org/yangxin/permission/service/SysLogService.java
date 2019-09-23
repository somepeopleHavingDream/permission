package org.yangxin.permission.service;

import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.LogType;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysLogMapper;
import org.yangxin.permission.model.SysAclModule;
import org.yangxin.permission.model.SysDept;
import org.yangxin.permission.model.SysLogWithBLOBs;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.util.IpUtil;
import org.yangxin.permission.util.JsonMapper;

import javax.annotation.Resource;
import java.util.Date;

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

    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
//        sysLog.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//        sysLog.setOperatorTime(new Date());
//        sysLog.setStatus(1);
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
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        // todo 做到这里要开始重构obj2String方法了
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
//        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
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
     * 操作者、Ip、操作时间、操作状态
     */
    private void setOperationAndStatus(SysLogWithBLOBs sysLog) {
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperatorTime(new Date());
        sysLog.setStatus(1);
    }
}
