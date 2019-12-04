package cn.edu.jxust.arrangeproduce.controller.backend;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ZSS
 * @date 2019/12/3 15:07
 * @description 管理员控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public AdminController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    /**
     * 添加管理员
     *
     * @param registerVo 注册实体
     * @param result     错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission("admin")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse registerAdmin(@Valid @RequestBody RegisterVo registerVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            Boolean existInDb = userService.existInDb(registerVo.getUsername());
            if (!existInDb) {
                String userId = UUIDUtil.getId();
                try {
                    userService.createUser(User.builder()
                            .userId(userId)
                            .userName(registerVo.getUsername())
                            .phone(registerVo.getPhone())
                            .role(Const.Role.ROLE_ADMIN)
                            .build());
                    accountService.createAccount(Account.builder()
                            .accountId(userId)
                            .accountName(registerVo.getUsername())
                            .password(EncryptionUtil.encrypt(registerVo.getPassword()))
                            .build());
                    log.info("添加管理员 :{} 成功", registerVo.getUsername());
                    return ServerResponse.createBySuccessMessage("添加管理员成功");
                } catch (Exception e) {
                    log.error("create user error []", e);
                    return ServerResponse.createByErrorMessage("添加管理员异常");
                }
            } else {
                return ServerResponse.createByErrorMessage("该用户名已存在,请修改后再注册");
            }
        }
    }
}
