package cn.edu.jxust.arrangeproduce.repository;

import cn.edu.jxust.arrangeproduce.entity.po.WorkLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZSS
 * @date 2020/1/17 19:45
 * @description 工作量实体化
 */
@Repository
public interface WorkLoadRepository extends JpaRepository<WorkLoad, String> {

}
