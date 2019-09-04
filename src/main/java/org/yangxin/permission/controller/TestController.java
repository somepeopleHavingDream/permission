package org.yangxin.permission.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yangxin.permission.common.JsonData;
import org.yangxin.permission.exception.ParamException;
import org.yangxin.permission.exception.PermissionException;
import org.yangxin.permission.param.TestVO;
import org.yangxin.permission.util.BeanValidator;

/**
 * 测试类
 *
 * @author yangxin
 * 2019/09/01 12:34
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        throw new PermissionException("test exception");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVO testVO) throws ParamException {
        log.info("validate");
        BeanValidator.check(testVO);
        return JsonData.success("test validate");
    }
}
