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
 * @date 2019/12/3 11:04
 * @description 企业实体
 */
@Entity(name = "ap_enterprise")
@org.hibernate.annotations.Table(appliesTo = "ap_enterprise", comment = "用户信息表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Enterprise implements Serializable {

    /**
     * 企业Id
     */
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "varchar(255) comment '企业Id'")
    private String enterpriseId;

    /**
     * 企业名称
     */
    @Column(nullable = false, columnDefinition = "varchar(100) comment '企业名'")
    private String enterpriseName;

    /**
     * 企业地址
     */
    @Column(columnDefinition = "varchar(255) comment '企业地址'")
    private String address;

    /**
     * 企业邮箱
     */
    @Column(columnDefinition = "varchar(100) comment '企业邮箱'")
    private String email;

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
