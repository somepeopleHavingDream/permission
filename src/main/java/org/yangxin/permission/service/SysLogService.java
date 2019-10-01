package org.yangxin.permission.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.LogType;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.beans.PageResult;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.*;
import org.yangxin.permission.dto.SearchLogDto;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.model.*;
import org.yangxin.permission.param.SearchLogParam;
import org.yangxin.permission.util.BeanValidator;
import org.yangxin.permission.util.GsonUtil;
import org.yangxin.permission.util.IpUtil;
import org.yangxin.permission.util.JsonMapper;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleAclService sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;

    public void recover(int id) {
        SysLogWithBLOBs sysLog = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLog, "待还原的记录不存在");
        switch (sysLog.getType()) {
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysDept afterDept = GsonUtil.string2Obj(sysLog.getOldValue(), SysDept.class);
//                SysDept afterDept = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysDept>()
                afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterDept.setOperateTime(new Date());
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
//                SysUser afterUser = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysUser>() {
//                });
                SysUser afterUser = GsonUtil.string2Obj(sysLog.getOldValue(), SysUser.class);
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAclModule, "待还原的权限模块已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
//                SysAclModule afterAclModule = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysAclModule>() {
//                });
                SysAclModule afterAclModule = GsonUtil.string2Obj(sysLog.getOldValue(), SysAclModule.class);
                afterAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAclModule.setOperateTime(new Date());
                sysAclModuleMapper.updateByPrimaryKeySelective(afterAclModule);
                saveAclModuleLog(beforeAclModule, afterAclModule);
                break;
            case LogType.TYPE_ACL:
                SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限点已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
//                SysAcl afterAcl = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysAcl>() {
//                });
                SysAcl afterAcl = GsonUtil.string2Obj(sysLog.getOldValue(), SysAcl.class);
                afterAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAcl.setOperateTime(new Date());
                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
//                SysRole afterRole = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysRole>() {
//                });
                SysRole afterRole = GsonUtil.string2Obj(sysLog.getOldValue(), SysRole.class);
                afterRole.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterRole.setOperateTime(new Date());
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(aclRole, "角色已经不存在了");
//                sysRoleAclService.changeRoleAcls(sysLog.getTargetId(), JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
//                }));
                sysRoleAclService.changeRoleAcls(sysLog.getTargetId(), GsonUtil.str2List(sysLog.getOldValue(), Integer.class));
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole userRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(userRole, "角色已经不存在了");
                sysRoleUserService.changeRoleUsers(sysLog.getTargetId(), JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            default:
        }
    }

    public PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page) {
        BeanValidator.check(page);
        SearchLogDto dto = new SearchLogDto();
        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeg())) {
            dto.setBeforeSeg("%" + param.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getAfterSeg())) {
            dto.setAfterSeg("%" + param.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            dto.setOperator("%" + param.getOperator() + "%");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFromTime())) {
                dto.setFromTime(dateFormat.parse(param.getFromTime()));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                dto.setToTime(dateFormat.parse(param.getToTime()));
            }
        } catch (Exception e) {
            throw new ParamException("传入的日期格式有问题，正确格式为：yyyy-MM-dd HH:mm:ss");
        }
        int count = sysLogMapper.countBySearchDto(dto);
        if (count > 0) {
            List<SysLogWithBLOBs> logList = sysLogMapper.getPageListBySearchDto(dto, page);
            return PageResult.<SysLogWithBLOBs>builder().total(count).data(logList).build();
        }
        return PageResult.<SysLogWithBLOBs>builder().build();
    }

    void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : GsonUtil.obj2String(before));
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : GsonUtil.obj2String(after));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : GsonUtil.obj2String(before));
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : GsonUtil.obj2String(after));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : GsonUtil.obj2String(before));
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : GsonUtil.obj2String(after));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : GsonUtil.obj2String(before));
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : GsonUtil.obj2String(after));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : GsonUtil.obj2String(before));
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : GsonUtil.obj2String(after));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    public void saveRoleAclLog(Integer roleId, List<Integer> originAclIdList, List<Integer> aclIdList) {
    }

    public void saveRoleUserLog(int roleId, List<Integer> originUserIdList, List<Integer> userIdList) {
    }
}
