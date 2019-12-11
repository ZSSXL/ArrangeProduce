package cn.edu.jxust.arrangeproduce.common.aspect;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZSS
 * @date 2019/12/3 9:53
 * @description 身份校验拦截
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    private final TokenUtil tokenUtil;

    @Autowired
    public PermissionAspect(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Around(value = "@annotation(cn.edu.jxust.arrangeproduce.annotation.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解中的参数
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        RequiredPermission requiredPermission = ms.getMethod().getAnnotation(RequiredPermission.class);
        String permission = requiredPermission.value();

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            String token = request.getHeader("token");
            if (tokenUtil.isValid(token)) {
                String role = tokenUtil.getClaim(token, "role").asString();
                if (StringUtils.equals(permission, role)) {
                    return joinPoint.proceed();
                } else {
                    return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "权限错误");
                }
            } else {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
            }
        } else {
            log.error("权限校验拦截失败");
            return ServerResponse.createByErrorMessage("权限校验拦截失败，请重新发送请求");
        }
    }

}
