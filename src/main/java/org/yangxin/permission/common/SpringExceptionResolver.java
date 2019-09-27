package org.yangxin.permission.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.exception.PermissionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 *
 * @author yangxin
 * 2019/09/01 12:09
 */
@Slf4j
@ControllerAdvice
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object o,
                                         Exception e) {
        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";

        // 这里我们要求项目中所有请求json数据，都使用.json结尾
        log.info("url: [{}]", url);
        if (url.endsWith(".json")) {
            if (e instanceof PermissionException || e instanceof ParamException) {
                JsonData result = JsonData.fail(e.getMessage());

                // 设置返回的数据为json类型，也可以不设置，返回对象
                mv = new ModelAndView(new MappingJackson2JsonView());
                mv.addObject(result.toMap());
            } else {
                log.error("unknown json exception, url: " + url, e);

                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView(new MappingJackson2JsonView());
                mv.addObject(result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("unknown page exception, url: " + url, e);

            // 这里我们要求项目中所有请求page页面，都使用.page结尾
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        } else {
            log.error("unknown exception, url: " + url, e);

            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView(new MappingJackson2JsonView());
            mv.addObject(result.toMap());
        }

        return mv;
    }
}
