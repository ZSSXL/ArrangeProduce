package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author ZSS
 * @date 2019/12/4 16:39
 * @description machine Vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MachineVo {

    @NotEmpty
    private String machineName;

    @NotEmpty
    private Integer machineNumber;

}
