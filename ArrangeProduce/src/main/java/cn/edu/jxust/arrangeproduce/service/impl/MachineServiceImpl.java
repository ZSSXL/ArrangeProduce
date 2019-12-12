package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Machine;
import cn.edu.jxust.arrangeproduce.repository.MachineRepository;
import cn.edu.jxust.arrangeproduce.service.MachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/4 16:32
 * @description Machine 服务层接口实现
 */
@Slf4j
@Service
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    public ServerResponse createMachine(Machine machine) {
        machineRepository.save(machine);
        return ServerResponse.createBySuccessMessage("添加小拉机成功");
    }

    @Override
    public ServerResponse<List<Machine>> getAllMachineByEnterpriseIdAndSort(String enterpriseId, String sort) {
        List<Machine> list = machineRepository.findAllByEnterpriseIdAndSortOrderByMachineNumber(enterpriseId, sort);
        if (list != null) {
            return ServerResponse.createBySuccess(list);
        } else {
            return ServerResponse.createByErrorMessage("查询机器失败");
        }
    }

    @Override
    public Boolean existInDb(String enterpriseId, Integer machineNumber) {
        return machineRepository.findByEnterpriseIdAndMachineNumber(enterpriseId, machineNumber).isPresent();
    }

    @Override
    public Boolean deleteAllMachineByEnterpriseId(String enterpriseId) {
        Integer integer = machineRepository.deleteAllByEnterpriseId(enterpriseId);
        log.info("delete all machine result : {}", integer);
        return integer > 0;
    }

    @Override
    public Boolean deleteMachineByMachineId(String machineId) {
        Integer integer = machineRepository.deleteByMachineId(machineId);
        log.info("delete machine result : {}", integer);
        return integer > 0;
    }
}
