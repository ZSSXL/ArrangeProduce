package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 删除该企业的所有用户
     *
     * @param enterpriseId 企业Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "delete au, aa from ap_user au, ap_account aa where au.user_id = aa.account_id and au.enterprise_id = ?1", nativeQuery = true)
    Integer deleteAllByEnterpriseId(String enterpriseId);

    /**
     * 删除用户
     *
     * @param userId 用户Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "delete au, aa from ap_user au, ap_account aa where au.user_id = aa.account_id and au.user_id = ?1", nativeQuery = true)
    Integer deleteByUserId(String userId);

    /**
     * 获取该企业所有用户
     *
     * @param enterpriseId 企业Id
     * @param role         角色
     * @param pageable     分页信息
     * @return Page<User>
     */
    Page<User> findAllByEnterpriseIdAndRoleOrderByCreateTimeDesc(String enterpriseId, String role, Pageable pageable);

    /**
     * 更新用户信息
     *
     * @param phone  电话
     * @param userId 用户Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update ap_user au set au.phone = ?1 where au.user_id = ?2 ", nativeQuery = true)
    Integer updateInfo(String phone, String userId);

    /**
     * 管理员分页获取所有主管
     *
     * @param role     主管权限
     * @param pageable 分页信息
     * @return Page<User>
     */
    Page<User> findAllByRole(String role, Pageable pageable);
}
