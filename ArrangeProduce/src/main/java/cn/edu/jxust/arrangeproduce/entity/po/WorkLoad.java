package cn.edu.jxust.arrangeproduce.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2020/1/17 15:31
 * @description 工作量记录实体
 */
@Entity(name = "ap_work_load")
@org.hibernate.annotations.Table(appliesTo = "ap_work_load", comment = "机器表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class WorkLoad implements Serializable {

    /**
     * 工作量Id
     */
    @Id
    @Column(columnDefinition = "varchar(255) comment '工作量Id'")
    private String workId;

    /**
     * 员工Id
     */
    @Column(columnDefinition = "varchar(255) comment '员工工号'")
    private String employeeNumber;

    /**
     * 数据
     */
    @Column(columnDefinition = "varchar(255) comment '数据'")
    private String data;

    /**
     * 创建日期
     */
    @Column(updatable = false, columnDefinition = "bigint(20) comment'创建时间'")
    @CreatedDate
    private Long createTime;
    /**
     * 修改日期
     */
    @LastModifiedDate
    @Column(columnDefinition = "bigint(20) comment'创建时间'")
    private Long updateTime;
}
