package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.Arrange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZSS
 * @date 2019/11/30 9:43
 * @description 排产数据实体化
 */
@Repository
public interface ArrangeRepository extends JpaRepository<Arrange, String> {
}
