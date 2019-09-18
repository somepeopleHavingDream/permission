package org.yangxin.permission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理员Controller
 *
 * @author yangxin
 * 2019/09/18 11:14
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /**
     * 进入管理员主界面
     */
    @RequestMapping("index.page")
    public ModelAndView index() {
        return new ModelAndView("admin");
    }
}
