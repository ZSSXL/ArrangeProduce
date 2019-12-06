package cn.edu.jxust.arrangeproduce.annotation;

import java.lang.annotation.*;

/**
 * @author ZSS
 * @date 2019/12/3 9:12
 * @description 自定义注解 身份校验
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiredPermission {

    String value() default "manager";

}
