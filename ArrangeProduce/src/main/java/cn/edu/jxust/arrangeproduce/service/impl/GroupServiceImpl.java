package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Group;
import cn.edu.jxust.arrangeproduce.repository.GroupRepository;
import cn.edu.jxust.arrangeproduce.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZSS
 * @date 2020/1/2 20:27
 * @description 分组服务层方法实现层
 */
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public ServerResponse createGroup(Group group) {
        groupRepository.save(group);
        return ServerResponse.createBySuccessMessage("新建成功");
    }

    @Override
    public ServerResponse<List<Group>> getAllGroupByEnterpriseId(String enterpriseId) {
        List<Group> groupList = groupRepository.findAllByEnterpriseIdOrderByGroupNameDesc(enterpriseId);
        if (groupList == null) {
            return ServerResponse.createByErrorMessage("查询失败");
        } else {
            return ServerResponse.createBySuccess("查询成功", groupList);
        }
    }

    @Override
    public Boolean deleteGroupById(String groupId) {
        Integer integer = groupRepository.deleteByGroupId(groupId);
        log.info("delete group : [{}] result : [{}]", groupId, integer > 0);
        return integer > 0;
    }

    @Override
    public Boolean checkGroupNumber(String enterpriseId, String groupNumber) {
        return groupRepository.findByGroupNumberAndEnterpriseId(groupNumber, enterpriseId).isPresent();
    }

    @Override
    public Boolean checkGroupName(String enterpriseId, String groupName) {
        return groupRepository.findByGroupNameAndEnterpriseId(groupName, enterpriseId).isPresent();
    }
}
