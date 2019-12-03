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
 * @date 2019/12/3 10:44
 * @description 账户实体
 */
@Entity(name = "ap_account")
@org.hibernate.annotations.Table(appliesTo = "ap_account", comment = "账户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Account implements Serializable {

    /**
     * 用户Id
     */
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "varchar(255) comment '用户id'")
    private String accountId;

    /**
     * 用户密码
     */
    @Column(nullable = false, columnDefinition = "varchar(100) comment '用户名'")
    private String accountName;

    /**
     * 用户密码
     */
    @Column(columnDefinition = "varchar(255) comment '用户没密码'")
    private String password;

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
