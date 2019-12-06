package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ZSS
 * @date 2019/12/4 9:17
 * @description arrange Vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class ArrangeVo {

    /**
     * 安排日期, 精确到日
     */
    private Long arrangeDate;

    /**
     * 安排小拉机
     */
    private String machine;

    /**
     * 设定线规
     */
    private String gauge;

    /**
     * 安排重量
     */
    private String weight;

    /**
     * 安排班次 A为早班，B为晚班
     */
    private String shift;

    /**
     * 公差
     */
    private String tolerance;

}
