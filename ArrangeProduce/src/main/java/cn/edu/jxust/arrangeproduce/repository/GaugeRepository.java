package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Gauge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/4 16:10
 * @description Gauge 持久化
 */
@Repository
public interface GaugeRepository extends JpaRepository<Gauge, String> {

    /**
     * 查看该企业中是否已存在该线规
     *
     * @param enterpriseId 企业id
     * @param gauge        线规
     * @return Optional<Gauge>
     */
    Optional<Gauge> findByEnterpriseIdAndGauge(String enterpriseId, BigDecimal gauge);

    /**
     * 排序后查询全部线规
     *
     * @param enterpriseId 企业Id
     * @return List<Gauge>
     */
    List<Gauge> findAllByEnterpriseIdOrderByGaugeDesc(String enterpriseId);

}
