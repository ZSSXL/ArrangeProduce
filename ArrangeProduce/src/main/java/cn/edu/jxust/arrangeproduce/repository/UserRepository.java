package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/3 11:15
 * @description 用户信息持久化
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 查看是否已存在该用户名
     *
     * @param username 用户名
     * @return Optional<User>
     */
    Optional<User> findByUserName(String username);

}
