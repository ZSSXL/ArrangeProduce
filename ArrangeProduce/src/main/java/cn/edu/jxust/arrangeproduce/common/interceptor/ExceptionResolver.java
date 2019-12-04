package cn.edu.jxust.arrangeproduce.common.interceptor;

import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZSS
 * @date 2019/12/3 16:02
 * @description 全局异常处理
 */
@Slf4j
@Component
@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, Exception e) {
        log.error("{} Exception", httpServletRequest.getRequestURI(), e);
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg", "接口异常，详情请查看服务端日志的异常信息");
        modelAndView.addObject("data", e.getClass());
        return modelAndView;
    }
}
