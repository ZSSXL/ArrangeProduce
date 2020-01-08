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
 * @date 2020/1/2 20:15
 * @description 退火机收线头分组表
 */
@Entity(name = "ap_group")
@org.hibernate.annotations.Table(appliesTo = "ap_group", comment = "退火机收线头分组表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Group implements Serializable {

    /**
     * 分组Id
     */
    @Id
    @Column(columnDefinition = "varchar(255) comment '分组Id'")
    private String groupId;

    /**
     * 分组组名
     */
    @Column(columnDefinition = "varchar(50) comment '分组组名'")
    private String groupName;

    /**
     * 编号
     */
    @Column(columnDefinition = "varchar(255) comment '编号'")
    private String groupNumber;

    /**
     * 企业Id
     */
    @Column(columnDefinition = "varchar(255) comment '企业Id'")
    private String enterpriseId;

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
