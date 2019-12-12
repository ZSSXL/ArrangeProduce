package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.util.RedisPoolUtil;
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZSS
 * @date 2019/12/12 10:04
 * @description 消息 控制器
 */
@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    private final TokenUtil tokenUtil;

    @Autowired
    public NoticeController(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    /**
     * 员工上线后查看是否有未读消息
     *
     * @param token token
     * @return ServeResponse
     */
    @GetMapping
    @RequiredPermission("employee")
    public ServerResponse<String> getMessage(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        String result = RedisPoolUtil.get(enterpriseId + "Arrange");
        if (StringUtils.isEmpty(result)) {
            return ServerResponse.createByErrorMessage("没有信息新消息");
        } else {
            return ServerResponse.createBySuccess("有新消息", result);
        }
    }

    /**
     * 员工查确认了消息后，删除Redis中的记录
     *
     * @param token token
     * @return ServerResponse
     */
    @DeleteMapping
    @RequiredPermission("employee")
    public ServerResponse delMessage(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        Long del = RedisPoolUtil.del(enterpriseId + "Arrange");
        if (del == 1) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("没有什么好删除的了");
        }
    }

}
