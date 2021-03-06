package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.LoginVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import cn.edu.jxust.arrangeproduce.util.MapUtil;
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ZSS
 * @date 2019/12/3 16:24
 * @description 账户控制器
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {

    private final AccountService accountService;
    private final UserService userService;
    private final TokenUtil tokenUtil;

    @Autowired
    public AccountController(AccountService accountService, UserService userService, TokenUtil tokenUtil) {
        this.accountService = accountService;
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 主管和员工登录
     *
     * @param loginVo 登录Vo
     * @param result  错误结果
     * @return ServerResponse<String>
     */
    @PostMapping
    public ServerResponse<String> login(@RequestBody @Valid LoginVo loginVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            ServerResponse<String> login = accountService.login(loginVo.getUsername(), EncryptionUtil.encrypt(loginVo.getPassword()));
            if (login.isSuccess()) {
                User user = userService.getUserById(login.getData());
                if (user != null) {
                    String token = tokenUtil.createJwt(MapUtil.create(
                            "tokenId", user.getUserId(),
                            "username", user.getUserName(),
                            "role", user.getRole(),
                            "enterpriseId", user.getEnterpriseId()));
                    if (StringUtils.isEmpty(token)) {
                        return ServerResponse.createByErrorMessage("登录失败，创建token失败");
                    } else {
                        return ServerResponse.createBySuccess(user.getUserId(), token);
                    }
                } else {
                    log.error("登录失败");
                    return ServerResponse.createByErrorMessage("登录失败");
                }
            } else {
                return login;
            }
        }
    }
}
