package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/11/30 9:43
 * @description 排产数据实体化
 */
@Repository
public interface ArrangeRepository extends JpaRepository<Arrange, String> {

    /**
     * 查看是否任务冲突
     *
     * @param arrangeDate  安排时间
     * @param shift        安排早/晚班
     * @param machine      安排小拉机
     * @param enterpriseId 所属企业
     * @return Optional<Arrange>
     */
    Optional<Arrange> findByArrangeDateAndShiftAndMachineAndEnterpriseId(Long arrangeDate, String shift, String machine, String enterpriseId);


    /**
     * 删除所有的排产信息
     *
     * @param enterpriseId 企业Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteAllByEnterpriseId(String enterpriseId);

    /**
     * 删除该排产信息
     *
     * @param arrangeId 排产Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByArrangeId(String arrangeId);

    /**
     * 分页获取该企业所有的排产信息
     *
     * @param enterpriseId 企业Id
     * @param pageable     分页信息
     * @return Page<Arrange>
     */
    Page<Arrange> findAllByEnterpriseId(String enterpriseId, Pageable pageable);

}
