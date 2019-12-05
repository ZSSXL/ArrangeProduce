package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.User;

/**
 * @author ZSS
 * @date 2019/12/3 13:46
 * @description User 服务层接口
 */
public interface UserService {

    /**
     * 创建用户
     *
     * @param user 用户实体
     * @return ServerResponse
     */
    ServerResponse createUser(User user);

    /**
     * 通过用户名查看用户
     *
     * @param username 用户名
     * @return Boolean
     */
    Boolean existInDb(String username);

    /**
     * 通过用户Id 查询用户信息
     *
     * @param userId 用户Id
     * @return User
     */
    User getUserById(String userId);

    /**
     * 删除该企业的所有用户
     *
     * @param enterpriseId 企业Id
     * @return Boolean
     */
    Boolean deleteAllUserByEnterpriseId(String enterpriseId);

    /**
     * 删除用户
     *
     * @param userId userId
     * @return Boolean
     */
    Boolean deleteUserById(String userId);
}
