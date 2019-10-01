package org.yangxin.permission.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yangxin.permission.beans.PageQuery;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.param.SearchLogParam;
import org.yangxin.permission.service.SysLogService;

import javax.annotation.Resource;

/**
 * 日志Controller
 *
 * @author yangxin
 * 2019/09/30 16:09
 */
@Controller
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {
    @Resource
    private SysLogService sysLogService;

    /**
     * 进入页面
     */
    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    /**
     * 恢复
     */
    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id) {
        log.info("id: [{}]", id);

        sysLogService.recover(id);
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData searchPage(SearchLogParam param, PageQuery page) {
        log.info("param: [{}], page: [{}]", param, page);

        return JsonData.success(sysLogService.searchPageList(param, page));
    }
}
