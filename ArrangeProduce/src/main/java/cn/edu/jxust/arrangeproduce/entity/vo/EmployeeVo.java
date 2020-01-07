package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author ZSS
 * @date 2020/1/7 10:52
 * @description 员工Vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVo {

    private String employeeId;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String phone;

    private String enterpriseId;

    @NotEmpty
    private String employeeNumber;

    @NotEmpty
    private String department;

    @NotEmpty
    private String post;

    @NotEmpty
    private String sex;
}
