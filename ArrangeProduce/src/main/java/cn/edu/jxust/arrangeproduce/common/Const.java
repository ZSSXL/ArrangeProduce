package cn.edu.jxust.arrangeproduce.common;

/**
 * @author ZSS
 * @date 2019/12/3 10:19
 * @description 常量类
 */
public class Const {

    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 默认当前页
     */
    public static final String DEFAULT_PAGE_NUMBER = "0";

    /**
     * 分页默认显示条数
     */
    public static final String DEFAULT_PAGE_SIZE = "20";

    /**
     * 角色
     */
    public interface Role {
        // 管理员
        String ROLE_ADMIN = "admin";
        // 车间主任
        String ROLE_MANAGER = "manager";
        // 员工，雇员
        String ROLE_EMPLOYEE = "employee";
    }

}
