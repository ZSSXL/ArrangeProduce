package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ZSS
 * @date 2019/12/4 16:23
 * @description Machine 持久化
 */
@Repository
public interface MachineRepository extends JpaRepository<Machine, String> {

    /**
     * 查询该企业是否已创建该小拉机
     *
     * @param enterpriseId  企业Id
     * @param machineNumber 小拉机号码
     * @return Optional<Machine>
     */
    Optional<Machine> findByEnterpriseIdAndMachineNumber(String enterpriseId, Integer machineNumber);

    /**
     * 查询该企业的所有设备,并排序
     *
     * @param enterpriseId 企业Id
     * @return List<Machine>
     */
    List<Machine> findAllByEnterpriseIdOrderByMachineNumber(String enterpriseId);

}
