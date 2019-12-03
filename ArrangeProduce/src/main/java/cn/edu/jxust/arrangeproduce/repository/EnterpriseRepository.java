package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZSS
 * @date 2019/12/3 16:56
 * @description 企业持久化
 */
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, String> {
}
