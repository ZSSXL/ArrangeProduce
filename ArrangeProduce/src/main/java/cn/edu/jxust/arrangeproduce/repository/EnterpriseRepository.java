package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/3 16:56
 * @description 企业持久化
 */
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, String> {

    /**
     * 通过名称查看该企业
     *
     * @param enterpriseName 企业名称
     * @return Optional<Enterprise>
     */
    Optional<Enterprise> findByEnterpriseName(String enterpriseName);

    /**
     * 删除该企业
     *
     * @param enterpriseId 企业Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByEnterpriseId(String enterpriseId);

}
