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
 * @date 2019/11/30 9:40
 * @description 排产实体
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
    @Column(unique = true, nullable = false, columnDefinition = "varchar(100) comment '排产Id'")
    private String arrangeId;

    /**
     * 小拉/退火/绕线
     */
    @Column(nullable = false, columnDefinition = "varchar(20) comment '小拉/退火/绕线' ")
    private String sort;

    /**
     * 安排日期, 精确到日
     */
    @Column(columnDefinition = "bigint(20) comment '安排日期, 精确到日'")
    private Long arrangeDate;

    /**
     * 安排班次 A为早班，B为晚班
     */
    @Column(columnDefinition = "varchar(10) comment '安排班次 1为早班，为晚班'")
    private String shift;

    /**
     * 安排小拉机
     */
    @Column(nullable = false, columnDefinition = "varchar(20) comment '安排小拉机'")
    private String machine;

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
     * 公差
     */
    @Column(columnDefinition = "varchar(10) default '0' comment '公差'")
    private String tolerance;

    /**
     * 所属企业
     */
    @Column(nullable = false, columnDefinition = "varchar(255) comment '所属企业'")
    private String enterpriseId;

    /**
     * 二维码打印状态
     */
    @Column(nullable = false, columnDefinition = "int(2) comment '打印状态，是否打印过，0为未打印，1为已打印'")
    private Integer status;

    /**
     * 是否已推送，yes/no
     */
    @Column(columnDefinition = "varchar(8) comment '是否已推送，yes/no'")
    private String push;

    /**
     * 创建者
     */
    @Column(nullable = false, columnDefinition = "varchar(100) comment '这条排产信息的创建者'")
    private String creator;

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
