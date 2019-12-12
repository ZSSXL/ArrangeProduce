package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.AwArrange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/12 9:20
 * @description awArrange 持久化
 */
@Repository
public interface AwArrangeRepository extends JpaRepository<AwArrange, String> {

    /**
     * 查看是否存在任务冲突
     *
     * @param arrangeDate  安排时间
     * @param shift        班次
     * @param machine      机器
     * @param enterpriseId 企业Id
     * @return Optional<AwArrange>
     */
    Optional<AwArrange> findByArrangeDateAndShiftAndMachineAndEnterpriseId(Long arrangeDate, String shift, String machine, String enterpriseId);

    /**
     * 分页查找所有分页信息
     *
     * @param enterpriseId 企业Id
     * @param pageable     分页信息
     * @return Page<AwArrange>
     */
    Page<AwArrange> findAllByEnterpriseIdOrderByCreateTimeDesc(String enterpriseId, Pageable pageable);

    /**
     * 查看已推送的产品信息
     *
     * @param enterpriseId 企业Id
     * @param push         推送状态
     * @param pageable     分页信息
     * @return Page<AwArrange>
     */
    Page<AwArrange> findAllByEnterpriseIdAndPushOrderByCreateTimeDesc(String enterpriseId, String push, Pageable pageable);

    /**
     * 删除一条排产信息
     *
     * @param awArrangeId Id
     * @return Integer
     */
    Integer deleteByAwArrangeId(String awArrangeId);
}
