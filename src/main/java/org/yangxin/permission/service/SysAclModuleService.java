package org.yangxin.permission.service;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysAclMapper;
import org.yangxin.permission.dao.SysAclModuleMapper;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.model.SysAclModule;
import org.yangxin.permission.param.AclModuleParam;
import org.yangxin.permission.util.BeanValidator;
import org.yangxin.permission.util.IpUtil;
import org.yangxin.permission.util.LevelUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 权限模块Service
 *
 * @author yangxin
 * 2019/09/20 10:15
 */
@Service
public class SysAclModuleService {
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增权限模块
     */
    public void save(AclModuleParam param) {
        // 校验
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        // 新建一个SysAclModule对象
        SysAclModule aclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        setOperation(aclModule);

        // 插入数据库
        sysAclModuleMapper.insertSelective(aclModule);
        sysLogService.saveAclModuleLog(null, aclModule);
    }

    /**
     * 更新权限模块
     */
    public void update(AclModuleParam param) {
        // 校验
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        // 更新前的SysAclModule对象
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "带更新的权限模块不存在");

        // 更新后的SysAclModule对象
        SysAclModule after = SysAclModule.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        setOperation(after);

        // 更新，记录
        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);
    }

    /**
     * 如果要保证事务生效，需要调整这个方法，一个可行的方法是重新创建一个Service类，然后把这个方法转移过去
     */
    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            String curLevel = before.getLevel() + "." + before.getId();
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(curLevel + "%");
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.equals(curLevel) || level.indexOf(curLevel + ".") == 0) {
                        // getChildAclModuleListByLevel可能会取出多余的内容，因此需要加个判断
                        // 比如0.1*，可能取出0.1、0.1.3、0.11、0.11.3，而期望取出0.1、0.1.3，因此需要判断等于0.1或者以0.1.为前缀才满足条件
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 删除权限模块
     */
    public void delete(int aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在，无法删除");
        if (sysAclModuleMapper.countByParentId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有子模块，无法删除");
        }
        if (sysAclMapper.countByAclModuleId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有用户，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

    /**
     * 操作人、操作人Ip、操作时间
     */
    private void setOperation(SysAclModule aclModule) {
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
    }

    /**
     * 获得权限模块级别
     *
     * @param aclModuleId 权限模块Id
     */
    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        return aclModule == null ? null : aclModule.getLevel();
    }

    /**
     * 是否存在一条“满足规则”的记录
     */
    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;
    }
}
