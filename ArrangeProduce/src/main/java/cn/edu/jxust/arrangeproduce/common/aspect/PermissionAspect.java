package cn.edu.jxust.arrangeproduce.common.aspect;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Admin;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ZSS
 * @date 2019/12/3 9:53
 * @description 身份校验拦截
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Around(value = "@annotation(cn.edu.jxust.arrangeproduce.annotation.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解中的参数
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        RequiredPermission requiredPermission = ms.getMethod().getAnnotation(RequiredPermission.class);
        String permission = requiredPermission.value();

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            HttpSession session = request.getSession();
            Object object = session.getAttribute(Const.CURRENT_USER);
            if (object == null) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
            } else {
                // 查看注解参数是否为admin
                // todo 普通用户访问admin接口会发生强制转换异常
                if (StringUtils.equals(permission, Const.Role.ROLE_ADMIN)) {
                    Admin admin = (Admin) object;
                    if (StringUtils.equals(permission, admin.getRole())) {
                        return joinPoint.proceed();
                    } else {
                        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "权限错误, 你不是管理员");
                    }
                } else {
                    User user = (User) object;
                    if (StringUtils.equals(permission, user.getRole())) {
                        return joinPoint.proceed();
                    } else {
                        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "权限错误, ");
                    }
                }
            }
        } else {
            log.error("权限校验拦截失败");
            return ServerResponse.createByErrorMessage("权限校验拦截失败，请重新发送请求");
        }
    }

}
