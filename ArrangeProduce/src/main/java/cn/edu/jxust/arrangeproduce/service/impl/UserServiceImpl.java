package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.repository.UserRepository;
import cn.edu.jxust.arrangeproduce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/3 13:46
 * @description User 服务层方法实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ServerResponse createUser(User user) {
        userRepository.save(user);
        return ServerResponse.createBySuccess();
    }

    @Override
    public Boolean existInDb(String username) {
        return userRepository.findByUserName(username).isPresent();
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Boolean deleteAllUserByEnterpriseId(String enterpriseId) {
        Integer integer = userRepository.deleteAllByEnterpriseId(enterpriseId);
        log.info("delete all user result : [{}]", integer > 0);
        return integer > 0;
    }

    @Override
    public Boolean deleteUserById(String userId) {
        Integer integer = userRepository.deleteByUserId(userId);
        log.info("delete user result : [{}]", integer > 0);
        return integer > 0;
    }

    @Override
    public ServerResponse<Page<User>> getAllUserByRole(String enterpriseId, String role, Pageable pageable) {
        Page<User> userPage = userRepository.findAllByEnterpriseIdAndRoleOrderByCreateTimeDesc(enterpriseId, role, pageable);
        if (userPage == null) {
            return ServerResponse.createByErrorMessage("查询失败");
        } else {
            return ServerResponse.createBySuccess(userPage);
        }
    }

    @Override
    public Boolean updateUserInfo(String phone, String userId) {
        Integer integer = userRepository.updateInfo(phone, userId);
        log.info("update user info result : [{}]", integer > 0);
        return integer > 0;
    }

    @Override
    public ServerResponse<Page<User>> getAllUserByPage(Pageable pageable) {
        Page<User> userPage = userRepository.findAllByRole(Const.Role.ROLE_MANAGER, pageable);
        return ServerResponse.createBySuccess(userPage);
    }

}
