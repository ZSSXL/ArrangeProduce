package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    Optional<Machine> findByEnterpriseIdAndMachineNumber(String enterpriseId, String machineNumber);

    /**
     * 查询该企业的所有设备,并排序
     *
     * @param enterpriseId 企业Id
     * @param sort         机器种类
     * @return List<Machine>
     */
    List<Machine> findAllByEnterpriseIdAndSortOrderByMachineNumber(String enterpriseId, String sort);

    /**
     * 删除该企业的所有小拉机
     *
     * @param enterpriseId 企业Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteAllByEnterpriseId(String enterpriseId);

    /**
     * 删除该小拉机
     *
     * @param machineId 小拉机Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByMachineId(String machineId);

    /**
     * 获取设备名
     *
     * @param machineNumber 设备编码
     * @param enterpriseId  企业Id
     * @return Machine
     */
    Optional<Machine> findByMachineNumberAndEnterpriseId(String machineNumber, String enterpriseId);

    /**
     * 通过名称查询机器
     *
     * @param machineName  机器名称
     * @param enterpriseId 企业Id
     * @return Optional<Machine>
     */
    Optional<Machine> findByMachineNameAndEnterpriseId(String machineName, String enterpriseId);
}
