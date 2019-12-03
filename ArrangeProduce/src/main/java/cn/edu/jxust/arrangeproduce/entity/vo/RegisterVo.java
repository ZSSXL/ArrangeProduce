package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author ZSS
 * @date 2019/12/3 13:31
 * @description 管理员注册实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVo {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String phone;

    private String enterpriseId;

}
