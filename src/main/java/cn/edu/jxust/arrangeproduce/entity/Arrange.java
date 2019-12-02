package cn.edu.jxust.arrangeproduce.entity;

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
 * @date 2019/11/30 9:40
 * @description
 */
@Entity(name = "ap_arrange")
@org.hibernate.annotations.Table(appliesTo = "ap_arrange", comment = "排产表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Arrange implements Serializable {

    /**
     * 排产Id
     */
    @Id
    @Column(columnDefinition = "varchar(100) comment '排产Id'")
    private String arrangeId;

    /**
     * 安排日期, 精确到日
     */
    @Column(columnDefinition = "bigint(20) comment '安排日期, 精确到日'")
    private Long arrangeDate;

    /**
     * 安排小拉机
     */
    @Column(columnDefinition = "varchar(20) comment '安排小拉机'")
    private String machine;

    /**
     * 设定线规
     */
    @Column(columnDefinition = "varchar(10) comment '设定线规'")
    private String gauge;

    /**
     * 安排重量
     */
    @Column(columnDefinition = "varchar(20) comment '安排重量'")
    private String weight;

    /**
     * 安排班次 A为早班，B为晚班
     */
    @Column(columnDefinition = "varchar(10) comment '安排班次 A为早班，B为晚班'")
    private String shift;

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
