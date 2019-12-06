package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Gauge;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/4 16:13
 * @description Gauge 服务层接口
 */
public interface GaugeService {

    /**
     * 添加线规
     *
     * @param gauge 线规实体
     * @return ServerResponse
     */
    ServerResponse createGauge(Gauge gauge);

    /**
     * 获取企业所有的线规
     *
     * @param enterpriseId 企业Id
     * @return ServerResponse<List < Gauge>>
     */
    ServerResponse<List<Gauge>> getAllGaugeByEnterpriseId(String enterpriseId);

    /**
     * 查看该线规是否在该企业中存在
     *
     * @param enterpriseId 企业id
     * @param gauge        线规
     * @return Boolean
     */
    Boolean existInDb(String enterpriseId, BigDecimal gauge);

    /**
     * 删除该企业的所有线规
     *
     * @param enterpriseId 企业Id
     * @return Boolean
     */
    Boolean deleteAllGaugeByEnterpriseId(String enterpriseId);

    /**
     * 删除该线规
     *
     * @param gaugeId 线规Id
     * @return Boolean
     */
    Boolean deleteGaugeByGaugeId(String gaugeId);

}
