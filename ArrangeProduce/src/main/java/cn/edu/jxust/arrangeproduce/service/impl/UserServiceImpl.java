package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.repository.UserRepository;
import cn.edu.jxust.arrangeproduce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/3 13:46
 * @description User 服务层方法实现
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ServerResponse createUser(User user) {
        User save = userRepository.save(user);
        if (save != null) {
            return ServerResponse.createBySuccessMessage("添加用户成功");
        } else {
            return ServerResponse.createByErrorMessage("添加用户失败");
        }
    }

    @Override
    public Boolean existInDb(String username) {
        return userRepository.findByUserName(username).isPresent();
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
