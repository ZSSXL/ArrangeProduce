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
     * 默认推送状态
     */
    public static final String DEFAULT_NO_PUSH = "no";

    /**
     * 分页默认显示条数
     */
    public static final String DEFAULT_PAGE_SIZE = "20";

    /**
     * 排序种类
     */
    public interface Sort {

        /**
         * 小拉机
         */
        String SORT_DRAW = "draw";

        /**
         * 退火机
         */
        String SORT_ANNEALING = "annealing";

        /**
         * 绕线机
         */
        String SORT_WINDING = "winding";

    }

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
