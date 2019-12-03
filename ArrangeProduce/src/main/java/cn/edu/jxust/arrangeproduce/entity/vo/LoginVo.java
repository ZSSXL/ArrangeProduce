package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ZSS
 * @date 2019/12/3 15:43
 * @description 登录实体Vo
 */
@Data
public class LoginVo {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
