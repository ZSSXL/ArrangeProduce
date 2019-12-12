package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.AwArrange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author ZSS
 * @date 2019/12/12 9:22
 * @description awArrange 服务层接口
 */
public interface AwArrangeService {

    /**
     * 添加退货绕线机排产
     *
     * @param awArrange 保存实体
     * @return ServerResponse
     */
    ServerResponse createAwArrange(AwArrange awArrange);

    /**
     * 查看是否存在任务冲突
     *
     * @param awArrangeDate 安排任务时间
     * @param shift         班次
     * @param machine       机器
     * @param sort          种类
     * @param enterpriseId  企业Id
     * @return Boolean
     */
    Boolean isConflict(Long awArrangeDate, String shift, String machine, String sort, String enterpriseId);

    /**
     * 分页获取所有的排产信息
     *
     * @param enterpriseId 企业Id
     * @param pageable     分页信息
     * @return ServerResponse<Page < AwArrange>>
     */
    ServerResponse<Page<AwArrange>> getAllAwArrangeByEnterpriseId(String enterpriseId, Pageable pageable);

    /**
     * 分页获取所有已经推送的排产信息
     *
     * @param enterpriseId 企业Id
     * @param push         推送状态
     * @param pageable     分页信息
     * @return ServerResponse<Page < AwArrange>>
     */
    ServerResponse<Page<AwArrange>> getAllAwArrangeByEnterpriseIdAndPush(String enterpriseId, String push, Pageable pageable);

    /**
     * 删除一天排产信息
     *
     * @param awArrangeId Id
     * @return Boolean
     */
    Boolean deleteAwArrangeByAwArrangeId(String awArrangeId);

    /**
     * 通过Id获取信息
     *
     * @param awArrangeId Id
     * @return AwArrange
     */
    AwArrange getAwArrangeById(String awArrangeId);
}
