package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Group;

import java.util.List;

/**
 * @author ZSS
 * @date 2020/1/2 20:21
 * @description 分组服务层接口
 */
public interface GroupService {

    /**
     * 新建分组
     *
     * @param group 分组实体
     * @return ServerResponse
     */
    ServerResponse createGroup(Group group);

    /**
     * 获取所有分组
     *
     * @param enterpriseId 企业Id
     * @return ServerResponse<List < Group>>
     */
    ServerResponse<List<Group>> getAllGroupByEnterpriseId(String enterpriseId);

    /**
     * 通过Id删除该分组
     *
     * @param groupId 分组Id
     * @return Boolean
     */
    Boolean deleteGroupById(String groupId);

    /**
     * 查看是否已经存在该分组
     *
     * @param enterpriseId 企业Id
     * @param groupNumber  分组详情
     * @return Boolean
     */
    Boolean checkGroupNumber(String enterpriseId, String groupNumber);

}
