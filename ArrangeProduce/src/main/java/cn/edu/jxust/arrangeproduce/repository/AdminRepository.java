package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/5 18:15
 * @description 管理员持久化
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    /**
     * 通过名称查找
     *
     * @param adminName 管理员名称
     * @return Optional<Admin>
     */
    Optional<Admin> findByAdminName(String adminName);

    /**
     * 通过Id删除管理员
     *
     * @param adminId 管理员Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByAdminId(String adminId);

}
