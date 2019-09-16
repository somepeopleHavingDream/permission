package org.yangxin.permission.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
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

/**
 * 部门树Service
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

    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }

        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,
                                   Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }

        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                aclDtoList.sort(Comparator.comparingInt(SysAcl::getSeq));
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }

    /**
     * 权限模型树
     */
    private List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {
        // 判空
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }

        // level -> [aclmodule1, aclmodul2, ...] Map<String, List<Object>>
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : dtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        rootList.sort(Comparator.comparingInt(SysAclModule::getSeq));
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;
    }

    /**
     * 转换成权限模型树
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
