package cn.edu.jxust.arrangeproduce.service;

/**
 * @author ZSS
 * @date 2019/12/3 16:57
 * @description Enterprise 服务层
 */
public interface EnterpriseService {

    /**
     * 该企业是否存在
     *
     * @param enterpriseId 企业id
     * @return Boolean
     */
    Boolean existInDbById(String enterpriseId);

}
