package org.yangxin.permission.service;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.beans.PageResult;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysUserMapper;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.param.UserParam;
import org.yangxin.permission.util.BeanValidator;
import org.yangxin.permission.util.IpUtil;
import org.yangxin.permission.util.MD5Util;
import org.yangxin.permission.util.PasswordUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户Service类
 *
 * @author yangxin
 * 2019/09/15 12:13
 */
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 得到全部用户记录
     */
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }

    /**
     * 通过关键字查找一条用户记录
     *
     * @param keyword 关键字
     */
    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    /**
     * 根据部门Id，分页查询
     *
     * @param deptId  部门id
     * @param page 分页查询对象
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.check(page);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder()
                    .total(count)
                    .data(list)
                    .build();
        }
        return PageResult.<SysUser>builder().build();
    }

    /**
     * 更新用户
     */
    public void update(UserParam param) {
        // 参数校验
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        // 设值
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        setOperation(after);

        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before, after);
    }

    /**
     * 新增用户
     */
    public void save(UserParam param) {
        // 参数校验
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        // 设值
        String password = PasswordUtil.randomPassword();
        password = "12345678";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser user = SysUser.builder()
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(encryptedPassword)
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        setOperation(user);

        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null, user);
    }

    /**
     * 操作人、操作人Ip、操作时间
     */
    private void setOperation(SysUser after) {
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//        after.setOperatorIp("127.0.0.1");
        after.setOperatorTime(new Date());
    }

    /**
     * 检查邮箱地址
     */
    private boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    /**
     * 检查电话号码
     */
    private boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }
}
