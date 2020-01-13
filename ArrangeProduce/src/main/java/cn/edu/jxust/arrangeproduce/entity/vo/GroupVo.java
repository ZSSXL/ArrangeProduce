package cn.edu.jxust.arrangeproduce.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2020/1/2 20:45
 * @description group Vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupVo implements Serializable {

    @NotEmpty
    private String groupNumber;
}
