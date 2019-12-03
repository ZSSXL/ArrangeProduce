package cn.edu.jxust.arrangeproduce.controller.backend;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ZSS
 * @date 2019/12/3 15:04
 * @description 管理员 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public AdminUserController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    /**
     * 管理员创建用户
     *
     * @param registerVo 注册实体
     * @param result 错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission("admin")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse registerUser(@Valid @RequestBody RegisterVo registerVo, BindingResult result){
        if(result.hasErrors()) {
            return ServerResponse.createByErrorMessage("参数错误");
        } else if(StringUtils.isEmpty(registerVo.getEnterpriseId())) {
            return ServerResponse.createByErrorMessage("请选择企业");
        } else {
            Boolean existInDb = userService.isExistInDb(registerVo.getUsername());
            if(!existInDb) {
                String userId = UUIDUtil.getId();
                try {
                    userService.createUser(User.builder()
                            .userId(userId)
                            .userName(registerVo.getUsername())
                            .phone(registerVo.getPhone())
                            .enterpriseId(registerVo.getEnterpriseId())
                            .role(Const.Role.ROLE_USER)
                            .build());
                    accountService.createAccount(Account.builder()
                            .accountId(userId)
                            .accountName(registerVo.getUsername())
                            .password(EncryptionUtil.encrypt(registerVo.getPassword()))
                            .build());
                    return ServerResponse.createBySuccessMessage("添加用户成功");
                } catch (Exception e) {
                    log.error("create user error []", e);
                    return ServerResponse.createByErrorMessage("添加用户失败");
                }
            } else {
                return ServerResponse.createByErrorMessage("该用户名已存在,请修改后再注册");
            }
        }
    }
}
