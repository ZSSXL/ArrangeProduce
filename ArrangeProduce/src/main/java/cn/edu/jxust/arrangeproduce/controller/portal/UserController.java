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
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final TokenUtil tokenUtil;


    @Autowired
    public UserController(UserService userService, AccountService accountService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.accountService = accountService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 主任添加员工
     *
     * @param registerVo 注册实体
     * @param token      token
     * @param result     错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse registerUser(@Valid @RequestBody RegisterVo registerVo, @RequestHeader("token") String token, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            // 判断是否存在该用户，不存在则添加
            Boolean user = userService.existInDb(registerVo.getUsername());
            if (!user) {
                String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
                String userId = UUIDUtil.getId();
                try {
                    userService.createUser(User.builder()
                            .userId(userId)
                            .userName(registerVo.getUsername())
                            .phone(registerVo.getPhone())
                            .enterpriseId(enterpriseId)
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
     * @param phone 电话
     * @param token token
     * @return ServerResponse
     */
    @PutMapping("/info")
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateUserInfo(String phone, @RequestHeader("token") String token) {
        if (StringUtils.isEmpty(phone)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            String tokenId = tokenUtil.getClaim(token, "tokenId").asString();
            String username = tokenUtil.getClaim(token, "username").asString();
            try {
                return userService.createUser(User.builder()
                        .userId(tokenId)
                        .userName(username)
                        .role(Const.Role.ROLE_MANAGER)
                        .enterpriseId(enterpriseId)
                        .phone(phone)
                        .build());
            } catch (Exception e) {
                log.error("{} failed to modify personal information : {}", username, e.getMessage());
                return ServerResponse.createByErrorMessage("修改个人信息发生未知异常");
            }
        }
    }

    /**
     * 修改密码
     *
     * @param password 新密码
     * @param token    token
     * @return ServerResponse
     */
    @PutMapping("/pwd")
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updatePassword(String password, @RequestHeader("token") String token) {
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String tokenId = tokenUtil.getClaim(token, "tokenId").asString();
            String username = tokenUtil.getClaim(token, "username").asString();
            try {
                return accountService.createAccount(Account.builder()
                        .accountId(tokenId)
                        .accountName(username)
                        .password(EncryptionUtil.encrypt(password))
                        .build());
            } catch (Exception e) {
                log.error("{} failed to modify personal password : {}", username, e.getMessage());
                return ServerResponse.createByErrorMessage("修改个人密码发生未知异常");
            }
        }
    }

}
