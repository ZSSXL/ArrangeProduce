package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/3 11:15
 * @description 账户持久化
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * 校验用户名和密码
     *
     * @param username 用户名
     * @param password 密码
     * @return Optional<Account>
     */
    Optional<Account> findByAccountNameAndPassword(String username, String password);


    /**
     * 更新用户密码
     *
     * @param password  新密码
     * @param accountId 账户Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update ap_account aa set aa.password = ?1 where aa.account_id = ?2 ", nativeQuery = true)
    Integer updatePassword(String password, String accountId);

}
