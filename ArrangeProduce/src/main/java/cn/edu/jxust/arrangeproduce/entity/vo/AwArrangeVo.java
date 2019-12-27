package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ZSS
 * @date 2019/12/12 10:43
 * @description awArrange Vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwArrangeVo {
    /**
     * 退火/绕线 annealing/winding
     */
    @NotEmpty
    private String sort;

    /**
     * 安排日期, 精确到日
     */
    @NotNull
    private Long arrangeDate;

    /**
     * 安排的机器
     */
    @NotEmpty
    private String machine;

    /**
     * 设定线规
     */
    @NotEmpty
    private String gauge;

    /**
     * 安排重量
     */
    @NotEmpty
    private String weight;

    /**
     * 安排班次 A为早班，B为晚班
     */
    @NotEmpty
    private String shift;

    /**
     * 公差
     */
    @NotEmpty
    private String tolerance;
}
