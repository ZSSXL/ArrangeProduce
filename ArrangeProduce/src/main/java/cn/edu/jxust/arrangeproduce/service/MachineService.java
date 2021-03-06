package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Machine;

import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/4 16:27
 * @description Machine 服务层接口
 */
public interface MachineService {

    /**
     * 添加小拉机
     *
     * @param machine 小拉机实体
     * @return ServerResponse
     */
    ServerResponse createMachine(Machine machine);

    /**
     * 查询该企业的所有设备
     *
     * @param enterpriseId 企业ID
     * @param sort         机器种类
     * @return ServerResponse<List < Machine>>
     */
    ServerResponse<List<Machine>> getAllMachineByEnterpriseIdAndSort(String enterpriseId, String sort);

    /**
     * 查看该企业是否已存在该小拉机
     *
     * @param enterpriseId  企业Id
     * @param machineNumber 小拉机编号
     * @return Boolean
     */
    Boolean existInDb(String enterpriseId, String machineNumber);

    /**
     * 查看该企业机器是否存在重名
     *
     * @param enterpriseId 企业Id
     * @param machineName  机器名称
     * @return Boolean
     */
    Boolean existInDbByName(String enterpriseId, String machineName);

    /**
     * 删除该企业所有的小拉机
     *
     * @param enterpriseId 企业Id
     * @return Boolean
     */
    Boolean deleteAllMachineByEnterpriseId(String enterpriseId);

    /**
     * 删除该小拉机
     *
     * @param machineId 小拉机Id
     * @return Boolean
     */
    Boolean deleteMachineByMachineId(String machineId);

    /**
     * 通过设备Id和企业Id锁定设备
     *
     * @param number       设备id
     * @param enterpriseId 企业Id
     * @return String
     */
    String getMachineNameByNumAndEnterpriseId(String number, String enterpriseId);

    /**
     * 通过机器名称获取该机器的编号
     *
     * @param enterpriseId 企业Id
     * @param machineName  机器名称
     * @return String
     */
    String getMachineByName(String enterpriseId, String machineName);
}
