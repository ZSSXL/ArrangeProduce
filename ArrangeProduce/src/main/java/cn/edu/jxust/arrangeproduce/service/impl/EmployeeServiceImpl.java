package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Employee;
import cn.edu.jxust.arrangeproduce.repository.EmployeeRepository;
import cn.edu.jxust.arrangeproduce.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2020/1/7 9:57
 * @description 员工信息服务层方法实现
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ServerResponse createEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            log.error("create employee error : [{}]", e.getMessage());
            return ServerResponse.createByError();
        }
    }

    @Override
    public Boolean deleteEmpById(String employeeId) {
        Integer integer = employeeRepository.deleteByEmployeeId(employeeId);
        log.info("delete employee message : [{}]", integer > 0);
        return integer > 0;
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    @Override
    public Boolean isExistInDn(String enterpriseId, String employeeNumber) {
        return employeeRepository.findByEnterpriseIdAndEmployeeNumber(enterpriseId, employeeNumber).isPresent();
    }
}
