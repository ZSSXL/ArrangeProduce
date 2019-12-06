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
 * @date 2019/12/4 16:18
 * @description 设备实体
 */
@Entity(name = "ap_machine")
@org.hibernate.annotations.Table(appliesTo = "ap_machine", comment = "账户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Machine implements Serializable {

    /**
     * 小拉机Id
     */
    @Id
    @Column(columnDefinition = "varchar(255) comment '小拉机Id'")
    private String machineId;

    /**
     * 小拉机名称
     */
    @Column(columnDefinition = "varchar(50) comment '小拉机名称'")
    private String machineName;

    /**
     * 小拉机比那好
     */
    @Column(columnDefinition = "int(8) comment '小拉机编号，如：2001'")
    private Integer machineNumber;

    /**
     * 所属企业
     */
    @Column(columnDefinition = "varchar(255) comment '所属企业'")
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
