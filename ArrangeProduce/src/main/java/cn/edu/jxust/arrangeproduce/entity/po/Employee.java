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
 * @date 2020/1/7 9:38
 * @description 员工相关信息实体
 */
@Entity(name = "ap_employee")
@org.hibernate.annotations.Table(appliesTo = "ap_employee", comment = "员工信息表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Employee implements Serializable {

    /**
     * 员工Id
     */
    @Id
    @Column(columnDefinition = "varchar(255) comment '员工Id'")
    private String employeeId;

    /**
     * 员工编号
     */
    @Column(columnDefinition = "varchar(255) comment '员工编号'")
    private String employeeNumber;

    /**
     * 部门
     */
    @Column(columnDefinition = "varchar(100) comment '部门'")
    private String department;

    /**
     * 岗位
     */
    @Column(columnDefinition = "varchar(100) comment '岗位'")
    private String post;

    /**
     * 性别
     */
    @Column(columnDefinition = "varchar(4) comment '1 是男性，0 是女性 ，性别'")
    private String sex;

    /**
     * 所属企业
     */
    @Column(nullable = false, columnDefinition = "varchar(255) comment '所属企业'")
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
