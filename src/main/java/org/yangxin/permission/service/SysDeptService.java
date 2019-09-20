package org.yangxin.permission.service;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysDeptMapper;
import org.yangxin.permission.dao.SysUserMapper;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.model.SysDept;
import org.yangxin.permission.param.DeptParam;
import org.yangxin.permission.util.BeanValidator;
import org.yangxin.permission.util.IpUtil;
import org.yangxin.permission.util.LevelUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 部门Service类
 *
 * @author yangxin
 * 2019/09/06 10:40
 */
@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增部门
     */
    public void save(DeptParam param) {
        // 参数校验、判断该部门名称是否已经存在
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        // 将前端已有的值直接赋进对象中
        SysDept dept = SysDept.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        // 设置层级、操作人、操作人Ip、操作时间
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        setOperation(dept);

        // 往数据库插入相关记录
        sysDeptMapper.insertSelective(dept);
//        sysLogService.saveDeptLog(null, dept);
    }

    /**
     * 更新部门
     */
    public void update(DeptParam param) {
        // 参数校验、判断该部门名称是否已经存在
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        // 这里没看懂，为啥要两次校验，可能是源码冗余了
//        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
//            throw new ParamException("同一层级下存在相同名称的部门");
//        }

        // 设值
        SysDept after = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        setOperation(after);

        updateWithChild(before, after);
//        sysLogService.saveDeptLog(before, after);
    }

    /**
     * 操作人、操作人Ip、操作时间
     */
    private void setOperation(SysDept after) {
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
//        after.setOperator("system");
        after.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//        after.setOperatorIp("127.0.0.1");
        after.setOperatorTime(new Date());
    }

    /**
     * 如果要保证事务生效，需要调整这个方法，一个可行的方式是重新创建一个Service类，然后把这个方法转移过去
     */
    @Transactional
    public void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();

        // 如果该部门的层级更新了，则该部门下的子部门的层级都需要更新
        if (!Objects.equals(after.getLevel(), before.getLevel())) {
            // 当前层级
            String curLevel = before.getLevel() + "." + before.getId();

            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(curLevel + "%");
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (Objects.equals(level, curLevel) || level.indexOf(curLevel + ".") == 0) {
                        // getChildAclModuleListByLevel可能会取出多余的内容，因此需要加个判断
                        // 比如0.1*可能取出0.1、0.1.3、0.11、0.11.3，而期望取出0.1、0.1.3，因此需要判断等于0.1或者以0.1.为前缀才满足条件
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    /**
     * 根据部门Id得到该部门的层级
     *
     * @param deptId 部门Id
     */
    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        return dept == null ? null : dept.getLevel();
    }

    /**
     * 检查该部门名称是否存在
     *
     * @param parentId 父部门Id
     * @param deptName 部门名称
     * @param deptId 部门Id
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * 通过部门Id，删除该部门
     *
     * @param deptId 部门Id
     */
    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");

        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        if (sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
