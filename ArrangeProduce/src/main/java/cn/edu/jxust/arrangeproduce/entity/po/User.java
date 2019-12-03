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
 * @date 2019/12/3 10:58
 * @description 普通用户实体
 */
@Entity(name = "ap_user")
@org.hibernate.annotations.Table(appliesTo = "ap_user", comment = "用户信息表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    /**
     * 用户Id
     */
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "varchar(255) comment '用户Id'")
    private String userId;

    /**
     * 用户名
     */
    @Column(nullable = false, columnDefinition = "varchar(100) comment '用户名'")
    private String userName;

    /**
     * 企业id
     */
    @Column(columnDefinition = "varchar(255) comment '企业Id, 管理员可以为空'")
    private String enterpriseId;

    /**
     * 身份
     */
    @Column(nullable = false, columnDefinition = "varchar(10) comment '身份'")
    private String role;

    /**
     * 电话
     */
    @Column(columnDefinition = "varchar(50) comment '电话'")
    private String phone;

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
