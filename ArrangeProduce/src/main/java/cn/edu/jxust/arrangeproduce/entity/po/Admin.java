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
 * @date 2019/12/5 18:13
 * @description 管理员实体
 */
@Entity(name = "ap_admin")
@org.hibernate.annotations.Table(appliesTo = "ap_admin", comment = "账户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Admin implements Serializable {

    /**
     * Id
     */
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "varchar(255) comment 'Id'")
    private String adminId;

    /**
     * 用户名
     */
    @Column(nullable = false, columnDefinition = "varchar(100) comment '名称'")
    private String adminName;

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
