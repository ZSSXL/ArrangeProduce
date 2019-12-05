package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author ZSS
 * @date 2019/12/3 13:45
 * @description user 控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private final UserService userService;
    private final AccountService accountService;


    @Autowired
    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * 修改个人信息
     * @param phone 电话
     * @param session session
     * @return ServerResponse
     */
    @PutMapping("/info")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateUserInfo(String phone, HttpSession session) {
        if (StringUtils.isEmpty(phone)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            try {
                return userService.createUser(User.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .role(Const.Role.ROLE_USER)
                        .enterpriseId(user.getEnterpriseId())
                        .phone(phone)
                        .build());
            } catch (Exception e) {
                log.error("{} failed to modify personal information : {}", user.getUserName(), e.getMessage());
                return ServerResponse.createByErrorMessage("修改个人信息发生未知异常");
            }
        }
    }

    /**
     * 修改密码
     *
     * @param password 新密码
     * @param session session
     * @return ServerResponse
     */
    @PutMapping("/pwd")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updatePassword(String password, HttpSession session) {
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            try {
                return accountService.createAccount(Account.builder()
                        .accountId(user.getUserId())
                        .accountName(user.getUserName())
                        .password(EncryptionUtil.encrypt(password))
                        .build());
            } catch (Exception e) {
                log.error("{} failed to modify personal password : {}", user.getUserName(), e.getMessage());
                return ServerResponse.createByErrorMessage("修改个人密码发生未知异常");
            }
        }
    }

}
