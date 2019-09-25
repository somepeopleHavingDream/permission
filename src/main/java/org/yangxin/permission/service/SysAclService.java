package org.yangxin.permission.service;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.beans.PageResult;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.dao.SysAclMapper;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.model.SysAcl;
import org.yangxin.permission.param.AclParam;
import org.yangxin.permission.util.BeanValidator;
import org.yangxin.permission.util.IpUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 权限Service
 *
 * @author yangxin
 * 2019/09/20 10:27
 */
@Service
public class SysAclService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增权限
     */
    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下存在相同名称的权限点");
        }

        SysAcl acl = SysAcl.builder()
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        acl.setCode(generateCode());
        setOperation(acl);

        sysAclMapper.insertSelective(acl);
        sysLogService.saveAclLog(null, acl);
    }

    /**
     * 更新权限
     */
    public void update(AclParam param) {
        // 校验
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }

        // 更新前的SysAcl对象
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        // 更新后的SysAcl对象
        SysAcl after = SysAcl.builder()
                .id(param.getId())
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        setOperation(after);

        // 数据库操作
        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before, after);
    }

    /**
     * 通过权限模块Id，分页查询数据
     */
    public Object getPageByAclModuleId(Integer aclModuleId, PageQuery page) {
        BeanValidator.check(page);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
            return PageResult.<SysAcl>builder()
                    .data(aclList)
                    .total(count)
                    .build();
        }
        return PageResult.<SysAcl>builder().build();
    }

    /**
     * 操作人、操作人Ip、操作时间
     */
    private void setOperation(SysAcl acl) {
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperatorTime(new Date());
    }

    /**
     * 生成code
     */
    private String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int) (Math.random() * 100);
    }

    /**
     * 检查该权限点是否存在
     *
     * @param aclModuleId 权限模块Id
     * @param name 权限名
     * @param id 权限Id
     */
    private boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }
}
