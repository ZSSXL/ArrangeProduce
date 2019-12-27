package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ZSS
 * @date 2019/12/27 20:22
 * @description 排产信息更新实体Vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVo {

    @NotEmpty
    private String arrangeId;

    @NotEmpty
    private String gauge;

    @NotEmpty
    private String tolerance;

    @NotEmpty
    private String weight;

    @NotNull
    private Long arrangeDate;

    @NotEmpty
    private String shift;

    @NotEmpty
    private String machineName;
}