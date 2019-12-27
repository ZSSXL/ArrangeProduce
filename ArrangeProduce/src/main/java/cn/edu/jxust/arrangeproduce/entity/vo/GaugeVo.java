package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ZSS
 * @date 2019/12/4 16:40
 * @description gauge Vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GaugeVo {

    @NotNull
    private BigDecimal gauge;

}
