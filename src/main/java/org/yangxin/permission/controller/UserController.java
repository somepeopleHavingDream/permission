package org.yangxin.permission.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.service.SysUserService;
import org.yangxin.permission.util.MD5Util;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 游客Controller
 *
 * @author yangxin
 * 2019/09/18 11:29
 */
@Controller
@Slf4j
public class UserController {
    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("logout");

        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);
    }

    /**
     * 游客登录
     */
    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 前端传过来的帐号和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("username: [{}]", username);

        // 查询数据库中对应记录
        SysUser sysUser = sysUserService.findByKeyword(username);
        log.info("sysUser: [{}]", sysUser);

        String errorMsg;
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不可以为空";
        } else if (sysUser == null) {
            errorMsg = "查询不到指定的用户";
        } else if (!Objects.equals(sysUser.getPassword(), MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户已被冻结，请联系管理员";
        } else {
            // login success
            log.info("登录成功, username: [{}]", sysUser.getUsername());

            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index.page");
            }
            return;
        }

        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);
    }
}
