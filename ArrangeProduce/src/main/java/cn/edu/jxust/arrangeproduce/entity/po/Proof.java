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
 * @date 2019/12/3 11:10
 * @description 防错实体
 */
@Entity(name = "ap_proof")
@org.hibernate.annotations.Table(appliesTo = "ap_proof", comment = "防错表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Proof  implements Serializable {

    /**
     * Id
     */
    @Id
    @Column(nullable = false, columnDefinition = "varchar(255) comment 'Id'")
    private String proofId;

    /**
     * 设定线规
     */
    @Column(nullable = false, columnDefinition = "varchar(10) comment '设定线规'")
    private String gauge;

    /**
     * 安排重量
     */
    @Column(nullable = false, columnDefinition = "varchar(20) comment '安排重量'")
    private String weight;


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
