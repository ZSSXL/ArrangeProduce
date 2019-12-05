package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Enterprise;

/**
 * @author ZSS
 * @date 2019/12/3 16:57
 * @description Enterprise 服务层
 */
public interface EnterpriseService {

    /**
     * 添加企业
     *
     * @param enterprise 企业实体
     * @return ServerResponse
     */
    ServerResponse createEnterprise(Enterprise enterprise);

    /**
     * 该企业是否存在
     *
     * @param enterpriseId 企业id
     * @return Boolean
     */
    Boolean existInDbById(String enterpriseId);

    /**
     * 该企业是否存在
     *
     * @param enterpriseName 企业名称
     * @return Boolean
     */
    Boolean existInDbByName(String enterpriseName);

}
