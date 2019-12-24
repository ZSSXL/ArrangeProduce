package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.AwArrange;
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
     * @param sort         分类
     * @param enterpriseId 企业Id
     * @return Optional<AwArrange>
     */
    Optional<AwArrange> findByArrangeDateAndShiftAndMachineAndSortAndEnterpriseId(Long arrangeDate, String shift, String machine, String sort, String enterpriseId);

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
    Page<AwArrange> findAllByEnterpriseIdAndPushOrderByArrangeDateDesc(String enterpriseId, String push, Pageable pageable);

    /**
     * 删除一条排产信息
     *
     * @param awArrangeId Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByAwArrangeId(String awArrangeId);

    /**
     * 更新打印状态
     *
     * @param awArrangeId  排产Id
     * @param enterpriseId 企业Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update ap_awarrange aa set aa.status = 1 where aa.aw_arrange_id = ?1 and aa.enterprise_id = ?2 ", nativeQuery = true)
    Integer updateStatus(String awArrangeId, String enterpriseId);

    /**
     * 更新推送状态
     *
     * @param push         推送
     * @param awArrangeId  id
     * @param enterpriseId 企业Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update ap_awarrange aa set aa.push = ?1 where aa.aw_arrange_id = ?2 and aa.enterprise_id = ?3 ", nativeQuery = true)
    Integer update(String push, String awArrangeId, String enterpriseId);
}
