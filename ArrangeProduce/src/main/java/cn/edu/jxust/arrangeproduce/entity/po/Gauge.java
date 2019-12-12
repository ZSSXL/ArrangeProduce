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
import java.math.BigDecimal;

/**
 * @author ZSS
 * @date 2019/12/4 16:05
 * @description 线规
 */
@Entity(name = "ap_gauge")
@org.hibernate.annotations.Table(appliesTo = "ap_gauge", comment = "线规表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Gauge implements Serializable {

    /**
     * 线规Id
     */
    @Id
    @Column(nullable = false, columnDefinition = "varchar(255) comment '线规Id'")
    private String gaugeId;

    /**
     * 线规
     */
    @Column(nullable = false, columnDefinition = "decimal(5,4) comment '线规'")
    private BigDecimal gauge;

    /**
     * 所属企业
     */
    @Column(nullable = false, columnDefinition = "varchar(255) comment '企业Id'")
    private String enterpriseId;

    /**
     * 创建日期
     */
    @Column(updatable = false, columnDefinition = "bigint(20) comment'创建时间'")
    @CreatedDate
    private Long createTime;
}
