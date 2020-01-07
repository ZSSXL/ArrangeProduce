package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Employee;

/**
 * @author ZSS
 * @date 2020/1/7 9:57
 * @description 员工信息服务层接口
 */
public interface EmployeeService {

    /**
     * 创建员工
     *
     * @param employee 员工信息实体
     * @return ServerResponse
     */
    ServerResponse createEmployee(Employee employee);

    /**
     * 删除员工信息
     *
     * @param employeeId 员工Id
     * @return Boolean
     */
    Boolean deleteEmpById(String employeeId);

    /**
     * 获取员工信息
     *
     * @param employeeId 员工Id
     * @return Employee
     */
    Employee getEmployeeById(String employeeId);

    /**
     * 查看员工号是否已经存在
     *
     * @param enterpriseId   企业Id
     * @param employeeNumber 员工编号
     * @return Boolean
     */
    Boolean isExistInDn(String enterpriseId, String employeeNumber);

}
