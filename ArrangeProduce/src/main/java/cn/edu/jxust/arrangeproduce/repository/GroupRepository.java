package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author ZSS
 * @date 2020/1/2 20:20
 * @description 退火机收线头分组持久化
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

    /**
     * 获取该企业所有的分组信息
     *
     * @param enterpriseId 企业Id
     * @return List<Group>
     */
    List<Group> findAllByEnterpriseIdOrderByGroupNameDesc(String enterpriseId);

    /**
     * 通过Id删除分组信息
     *
     * @param groupId 分组Id
     * @return Integer
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer deleteByGroupId(String groupId);

    /**
     * 通过企业Id和分组名称查询
     *
     * @param groupName    分组名称
     * @param enterpriseId 企业Id
     * @return Optional<Group>
     */
    Optional<Group> findByGroupNameAndEnterpriseId(String groupName, String enterpriseId);

    /**
     * 通过企业Id和分组信息查询
     *
     * @param groupNumber  分组信息
     * @param enterpriseId 企业Id
     * @return Optional<Group>
     */
    Optional<Group> findByGroupNumberAndEnterpriseId(String groupNumber, String enterpriseId);

}
