package cn.edu.jxust.arrangeproduce.aspect;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @author ZSS
 * @date 2019/12/3 9:53
 * @description 身份校验拦截
 */
@Aspect
@Component
public class PermissionAspect {

    @Around(value = "@annotation(cn.edu.jxust.arrangeproduce.annotation.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解中的参数
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        RequiredPermission requiredPermission = method.getAnnotation(RequiredPermission.class);
        String permission = requiredPermission.value();

        // 获取session
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            HttpSession session = request.getSession();
            session.getAttribute(Const.CURRENT_USER);

            // todo 判断是否登录，校验身份
            System.out.println(permission);
        }


        // 校验成功继续下一步
        return joinPoint.proceed();
    }

}
