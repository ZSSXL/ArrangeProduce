package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}
