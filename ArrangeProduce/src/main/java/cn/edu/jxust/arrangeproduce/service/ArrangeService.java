package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    String isConflict(Long arrangeDate, String shift, String machine, String enterpriseId);

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

    /**
     * 通过Id获取排产信息
     *
     * @param arrangeId 排产Id
     * @return Arrange
     */
    Arrange getArrangeById(String arrangeId);

    /**
     * 分页获取所有的排产信息
     *
     * @param enterpriseId 企业Id
     * @param pageable     分页信息
     * @return ServerResponse<List < Arrange>>
     */
    ServerResponse<Page<Arrange>> getAllArrangeByEnterpriseId(String enterpriseId, Pageable pageable);

    /**
     * 分页获取所有排产信息
     *
     * @param enterpriseId 企业Id
     * @param push         推送状态
     * @param pageable     分页信息
     * @return ServerResponse<Page < Arrange>>
     */
    ServerResponse<Page<Arrange>> getAllArrangeByEnterpriseIdAndPush(String enterpriseId, String push, Pageable pageable);

    /**
     * 更新推送状态
     *
     * @param enterpriseId  企业Id
     * @param arrangeIdList idList
     * @return ServerResponse
     */
    ServerResponse updatePush(String enterpriseId, List<String> arrangeIdList);

    /**
     * 更新打印状态
     *
     * @param arrangeId    排产Id
     * @param enterpriseId 企业Id
     * @return Boolean
     */
    Boolean updateStatus(String arrangeId, String enterpriseId);
}
