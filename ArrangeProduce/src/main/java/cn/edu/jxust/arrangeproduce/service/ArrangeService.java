package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;

/**
 * @author ZSS
 * @date 2019/11/30 9:59
 * @description Arrange 服务层接口
 */
public interface ArrangeService {

    /**
     * 新建排产任务
     *
     * @param arrange 排产实体
     * @return ServerResponse
     */
    ServerResponse createArrange(Arrange arrange);

    /**
     * 查看该企业是否任务冲突
     *
     * @param arrangeDate  安排的时间
     * @param shift        安排的早/晚班
     * @param machine      安排的机器
     * @param enterpriseId 所属企业
     * @return Boolean
     */
    Boolean isConflict(Long arrangeDate, String shift, String machine, String enterpriseId);

    /**
     * 删除该企业的所有的排产信息
     *
     * @param enterpriseId 企业Id
     * @return Boolean
     */
    Boolean deleteAllArrangeByEnterpriseId(String enterpriseId);

    /**
     * 删除排产信息
     *
     * @param arrangeId 排产Id
     * @return Boolean
     */
    Boolean deleteArrangeByArrangeId(String arrangeId);

}
