package org.yangxin.permission.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.dto.DeptLevelDto;
import org.yangxin.permission.param.DeptParam;
import org.yangxin.permission.service.SysDeptService;
import org.yangxin.permission.service.SysTreeService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门Controller类
 *
 * @author yangxin
 * 2019/09/06 10:38
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page() {
        log.info("进入部门管理页面");
        return new ModelAndView("dept");
    }

    /**
     * 新增部门
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param) {
        log.info("param: [{}]", param);

        sysDeptService.save(param);
        return JsonData.success();
    }

    /**
     * 获得部门树
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    /**
     * 更新部门
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam param) {
        log.info("param: [{}]", param);

        sysDeptService.update(param);
        return JsonData.success();
    }

    /**
     * 删除部门
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int deptId) {
        log.info("deptId: [{}]", deptId);

        sysDeptService.delete(deptId);
        return JsonData.success();
    }
}
