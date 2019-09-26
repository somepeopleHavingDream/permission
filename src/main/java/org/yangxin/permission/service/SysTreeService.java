package org.yangxin.permission.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.yangxin.permission.dao.SysAclMapper;
import org.yangxin.permission.dao.SysAclModuleMapper;
import org.yangxin.permission.dao.SysDeptMapper;
import org.yangxin.permission.dto.AclDto;
import org.yangxin.permission.dto.AclModuleLevelDto;
import org.yangxin.permission.dto.DeptLevelDto;
import org.yangxin.permission.model.SysAcl;
import org.yangxin.permission.model.SysAclModule;
import org.yangxin.permission.model.SysDept;
import org.yangxin.permission.util.LevelUtil;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 树
 *
 * @author yangxin
 * 2019/09/10 15:34
 */
@Service
@Slf4j
public class SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysCoreService sysCoreService;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;

    /**
     * 用户权限树
     *
     * @param userId 用户Id
     */
    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    /**
     * 角色树

     * @param roleId 角色Id
     */
    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1. 当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 2. 当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 3. 当前系统所有权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        // 当前用户已分配的权限点
        Set<Integer> userAclIdSet = userAclList.stream()
                .map(SysAcl::getId)
                .collect(Collectors.toSet());
        // 当前角色分配的权限点
        Set<Integer> roleAclIdSet = roleAclList.stream()
                .map(SysAcl::getId)
                .collect(Collectors.toSet());

        // 这里其实有个小技巧，如果数据库里相应的记录为空，mybatis返回多条数据时，集合始终不会为null的。mybatis是先创建一个集合，再将数据库中对应记录设值到集合中
        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList) {
            AclDto dto = AclDto.adapt(acl);

            // 如果该用户拥有这个权限，那我们可以让他看到这个权限
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);
            }

            // 如果该角色拥有这个权限，那我们将这个权限默认选中
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }

        return aclListToTree(aclDtoList);
    }

    /**
     * 权限列表转成树，返回AclModuleLevel对象列表
     */
    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        // 判空
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }

        // 获得权限模型树，这里aclModuleLevelList是指最顶层的权限模型列表
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        // 构建权限模型id -> 权限的映射
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }

        // 将权限带顺序地绑定到各个权限模块下
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);

        return aclModuleLevelList;
    }

    /**
     * 将权限带顺序地绑定到各个权限模块下
     */
    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,
                                   Multimap<Integer, AclDto> moduleIdAclMap) {
        // 判空
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }

        for (AclModuleLevelDto dto : aclModuleLevelList) {
            // 该权限模块下的权限
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());

            // 将aclDtoList中的对象按序号排序、并将aclDtoList设置进AclModuleLevelDto对象中
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                aclDtoList.sort(Comparator.comparingInt(SysAcl::getSeq));
                dto.setAclList(aclDtoList);
            }

            // 递归绑定
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }

    /**
     * 权限模型树
     */
    public List<AclModuleLevelDto> aclModuleTree() {
        // 获得所有权限模型记录
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();

        // 将所有的SysAclModule对象转换成AclModuleLevelDto对象
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }

        // 权限模型列表转树
        return aclModuleListToTree(dtoList);
    }

    /**
     * 权限模型列表转树
     */
    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {
        // 判空
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }

        // level -> [aclmodule1, aclmodul2, ...] Map<String, List<Object>>
        // level -> List<AclModuleLevelDto>
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        // 根权限模型列表
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : dtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 将根权限模型列表按照序号排序
        rootList.sort(Comparator.comparingInt(SysAclModule::getSeq));
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;
    }

    /**
     * 递归转换成权限模型树
     */
    private void transformAclModuleTree(List<AclModuleLevelDto> dtoList,
                                        String level,
                                        Multimap<String, AclModuleLevelDto> levelAclModuleMap) {
        for (AclModuleLevelDto dto : dtoList) {
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)) {
                tempList.sort(Comparator.comparingInt(SysAclModule::getSeq));
                dto.setAclModuleList(tempList);
                transformAclModuleTree(tempList, nextLevel, levelAclModuleMap);
            }
        }
    }

    /**
     * 部门树
     */
    public List<DeptLevelDto> deptTree() {
        // 查询出全部部门记录
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        log.info("deptList.size: [{}]", deptList.size());

        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    /**
     * 将部门集合转换成一个树集合
     */
    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        // 如果无部门记录，返回一个空元素的集合
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }

        // level -> [dept1, dept2, ...] Map<String, List<Object>>
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        // 将部门按照部门级别分类
        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);

            // 如果该部门是0级部门，将它添加进rootList集合中
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 将0级部门按照seq从小到大排序
        rootList.sort(Comparator.comparingInt(SysDept::getSeq));

        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    /**
     * level: 0, 0, all 0->0.1,0.2
     * level: 0.1
     * level: 0.2
     */
    private void transformDeptTree(List<DeptLevelDto> deptLevelList,
                                   String level,
                                   Multimap<String, DeptLevelDto> levelDeptMap) {
        for (DeptLevelDto deptLevelDto : deptLevelList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());

            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                tempDeptList.sort(Comparator.comparingInt(SysDept::getSeq));
                // 设置下一层的部门
                deptLevelDto.setDeptList(tempDeptList);
                // 进入到下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }
}
