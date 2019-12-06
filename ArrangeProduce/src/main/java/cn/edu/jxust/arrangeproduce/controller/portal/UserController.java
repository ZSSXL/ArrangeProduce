package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.RegisterVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
     * 主任添加员工
     *
     * @param registerVo 注册实体
     * @param result     错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse registerUser(@Valid @RequestBody RegisterVo registerVo, HttpSession session, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            // 判断是否存在该用户，不存在则添加
            Boolean user = userService.existInDb(registerVo.getUsername());
            if (!user) {
                User manager = (User) session.getAttribute(Const.CURRENT_USER);
                String userId = UUIDUtil.getId();
                try {
                    userService.createUser(User.builder()
                            .userId(userId)
                            .userName(registerVo.getUsername())
                            .phone(registerVo.getPhone())
                            .enterpriseId(manager.getEnterpriseId())
                            .role(Const.Role.ROLE_EMPLOYEE)
                            .build());
                    accountService.createAccount(Account.builder()
                            .accountId(userId)
                            .accountName(registerVo.getUsername())
                            .password(EncryptionUtil.encrypt(registerVo.getPassword()))
                            .build());
                    log.info("create employee : {} success", registerVo.getUsername());
                    return ServerResponse.createBySuccessMessage("添加员工成功");
                } catch (Exception e) {
                    log.error("create employee error []", e);
                    return ServerResponse.createByErrorMessage("添加员工失败");
                }
            } else {
                return ServerResponse.createByErrorMessage("该用员工已存在,请修改后再注册");
            }
        }
    }

    /**
     * 修改个人信息
     *
     * @param phone   电话
     * @param session session
     * @return ServerResponse
     */
    @PutMapping("/info")
    @RequiredPermission
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
                        .role(Const.Role.ROLE_MANAGER)
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
     * @param session  session
     * @return ServerResponse
     */
    @PutMapping("/pwd")
    @RequiredPermission
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
