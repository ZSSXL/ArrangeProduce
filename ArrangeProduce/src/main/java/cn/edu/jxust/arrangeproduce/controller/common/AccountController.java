package cn.edu.jxust.arrangeproduce.controller.common;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.controller.portal.BaseController;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.LoginVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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

    @Autowired
    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param loginVo 登录Vo
     * @param session session
     * @param result  错误结果
     * @return ServerResponse<String>
     */
    @PostMapping
    public ServerResponse<String> login(@RequestBody @Valid LoginVo loginVo, HttpSession session, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            ServerResponse<String> login = accountService.login(loginVo.getUsername(), EncryptionUtil.encrypt(loginVo.getPassword()));
            if (login.isSuccess()) {
                User user = userService.getUserById(login.getData());
                if (user != null) {
                    session.setAttribute(Const.CURRENT_USER, user);
                    return login;
                } else {
                    log.error("登录失败，保存session错误");
                    return ServerResponse.createByErrorMessage("登录失败, 保存session错误");
                }
            } else {
                return login;
            }
        }
    }

    /**
     * 退出
     *
     * @param session session
     * @return ServerResponse
     */
    @PostMapping("/logout")
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        log.info("退出登录");
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }
}
