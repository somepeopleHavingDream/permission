package org.yangxin.permission.filter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.permission.common.ApplicationContextHelper;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.common.RequestHolder;
import org.yangxin.permission.model.SysUser;
import org.yangxin.permission.service.SysCoreService;
import org.yangxin.permission.util.GsonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 权限控制过滤器
 *
 * @author yangxin
 * 2019/09/27 16:30
 */
@Slf4j
public class AclControlFilter implements Filter {

    // 白名单
    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();
    // 不需要授权的url
    private final static String noAuthUrl = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) {
        exclusionUrlSet.add(noAuthUrl);
        exclusionUrlSet.add("/signin.jsp");
        exclusionUrlSet.add("/admin/index.page");

//        exclusionUrlSet.add("/sys/user/save.json");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String servletPath = httpServletRequest.getServletPath();
        Map<String, String[]> requestMap = httpServletRequest.getParameterMap();

        // 如果请求访问的url在白名单内，直接放行
        if (exclusionUrlSet.contains(servletPath)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // 获取当前用户
        SysUser sysUser = RequestHolder.getCurrentUser();

        // 如果用户是游客（未登录），则进行未授权逻辑处理
        if (sysUser == null) {
            log.info("someone visit [{}], but no login, parameter: [{}]", servletPath, GsonUtil.obj2String(requestMap));
            noAuth(httpServletRequest, httpServletResponse);
            return;
        }

        // 获得SysCoreService类对象
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        Preconditions.checkNotNull(sysCoreService, "SysCoreService为null");

        // 无权限，做无权限操作
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            log.info("[{}] visit [{}], but no login, parameter: [{}]",
                    GsonUtil.obj2String(sysUser),
                    servletPath,
                    GsonUtil.obj2String(requestMap));
            noAuth(httpServletRequest, httpServletResponse);
            return;
        }

        // 有权限，则放行
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {}

    /**
     * 未授权处理逻辑
     */
    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if (servletPath.endsWith(".json")) {
            JsonData jsonData = JsonData.fail("没有访问权限，如需要访问，请联系管理员");

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(GsonUtil.obj2String(jsonData));
        } else {
            clientRedirect(noAuthUrl, response);
        }
    }

    /**
     * 客户端跳转
     */
    private void clientRedirect(String url, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }
}
