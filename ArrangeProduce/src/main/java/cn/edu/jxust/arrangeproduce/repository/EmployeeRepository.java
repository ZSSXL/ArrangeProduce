package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2020/1/7 9:53
 * @description 员工信息持久化
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    /**
     * 删除员工信息
     *
     * @param employeeId 员工Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByEmployeeId(String employeeId);

    /**
     * 查看员工编号是否已经存在
     *
     * @param enterpriseId   企业Id
     * @param employeeNumber 编号
     * @return Optional
     */
    Optional<Employee> findByEnterpriseIdAndEmployeeNumber(String enterpriseId, String employeeNumber);
}
