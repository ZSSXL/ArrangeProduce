package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;

/**
 * @author ZSS
 * @date 2019/12/3 11:29
 * @description Account 服务层接口
 */
public interface AccountService {

    /**
     * 创建账户
     *
     * @param account 账户实体
     * @return 创建账户
     */
    ServerResponse createAccount(Account account);

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return Boolean
     */
    ServerResponse<String> login(String username, String password);

    /**
     * 更新密码
     *
     * @param password  密码
     * @param accountId 用户Id
     * @return Boolean
     */
    Boolean updatePassword(String password, String accountId);

}
