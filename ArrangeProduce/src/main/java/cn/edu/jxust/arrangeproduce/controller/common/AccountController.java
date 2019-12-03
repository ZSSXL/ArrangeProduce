package cn.edu.jxust.arrangeproduce.controller.common;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.controller.portal.BaseController;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.LoginVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
     * @return ServerResponse
     */
    @PostMapping
    public ServerResponse login(@RequestBody @Valid LoginVo loginVo, HttpSession session, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorMessage("参数错误");
        } else {
            String userId = accountService.login(loginVo.getUsername(), EncryptionUtil.encrypt(loginVo.getPassword()));
            if (StringUtils.isNoneEmpty(userId)) {
                User user = userService.getUserById(userId);
                if (user != null) {
                    session.setAttribute(Const.CURRENT_USER, user);
                    log.info("登录成功");
                    return ServerResponse.createBySuccessMessage("登录成功");
                } else {
                    log.error("登录失败，保存session错误");
                    return ServerResponse.createByErrorMessage("登录失败, 保存session错误");
                }
            } else {
                return ServerResponse.createByErrorMessage("登录失败, 用户名和密码不匹配");
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
