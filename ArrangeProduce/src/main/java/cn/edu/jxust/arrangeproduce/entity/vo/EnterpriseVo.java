package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author ZSS
 * @date 2019/12/5 9:07
 * @description enterprise Vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseVo {

    @NotEmpty
    private String enterpriseName;

    @NotEmpty
    private String address;

    @NotEmpty
    private String email;

}
