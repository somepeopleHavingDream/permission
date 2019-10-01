package org.yangxin.permission.filter;

import lombok.extern.slf4j.Slf4j;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.model.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器
 *
 * @author yangxin
 * 2019/09/19 09:55
 */
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * 拦截没有登录的用户请求
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

//        filterChain.doFilter(servletRequest, servletResponse);

        SysUser sysUser = (SysUser) req.getSession().getAttribute("user");
        if (sysUser == null) {
            String path = "/signin.jsp";
            resp.sendRedirect(path);
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(req);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
