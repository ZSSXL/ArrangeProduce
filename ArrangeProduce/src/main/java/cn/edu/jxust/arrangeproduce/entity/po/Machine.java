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
@org.hibernate.annotations.Table(appliesTo = "ap_machine", comment = "机器表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Machine implements Serializable {

    /**
     * 机器Id
     */
    @Id
    @Column(columnDefinition = "varchar(255) comment '机器Id'")
    private String machineId;

    /**
     * 机器种类 小拉机/退火绕线机 draw/aw
     */
    @Column(columnDefinition = "varchar(20) comment '机器种类 小拉机 draw/退火绕线机 aw'")
    private String sort;

    /**
     * 小拉机名称
     */
    @Column(columnDefinition = "varchar(50) comment '机器名称'")
    private String machineName;

    /**
     * 小拉机比那好
     */
    @Column(columnDefinition = "varchar(8) comment '机器编号，如：2001/3001'")
    private String machineNumber;

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
