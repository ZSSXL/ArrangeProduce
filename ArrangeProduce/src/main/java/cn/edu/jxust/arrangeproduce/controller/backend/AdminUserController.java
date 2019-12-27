package cn.edu.jxust.arrangeproduce.controller.backend;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.RegisterVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.EnterpriseService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final EnterpriseService enterpriseService;

    @Autowired
    public AdminUserController(AccountService accountService, UserService userService, EnterpriseService enterpriseService) {
        this.accountService = accountService;
        this.userService = userService;
        this.enterpriseService = enterpriseService;
    }

    /**
     * 管理员创建用户
     *
     * @param registerVo 注册实体
     * @param result     错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission("admin")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse registerUser(@Valid @RequestBody RegisterVo registerVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else if (StringUtils.isEmpty(registerVo.getEnterpriseId())) {
            return ServerResponse.createByErrorMessage("请选择企业");
        } else {
            // 判断是否存在该企业，存在则添加
            Boolean enterpriseId = enterpriseService.existInDbById(registerVo.getEnterpriseId());
            if (!enterpriseId) {
                return ServerResponse.createByErrorMessage("该企业不存在");
            } else {
                // 判断是否存在该用户，不存在则添加
                Boolean user = userService.existInDb(registerVo.getUsername());
                if (!user) {
                    String userId = UUIDUtil.getId();
                    try {
                        userService.createUser(User.builder()
                                .userId(userId)
                                .userName(registerVo.getUsername())
                                .phone(registerVo.getPhone())
                                .enterpriseId(registerVo.getEnterpriseId())
                                .role(Const.Role.ROLE_MANAGER)
                                .build());
                        accountService.createAccount(Account.builder()
                                .accountId(userId)
                                .accountName(registerVo.getUsername())
                                .password(EncryptionUtil.encrypt(registerVo.getPassword()))
                                .build());
                        return ServerResponse.createBySuccessMessage("添加主管成功");
                    } catch (Exception e) {
                        log.error("create manager error []", e);
                        return ServerResponse.createByErrorMessage("添加主管失败");
                    }
                } else {
                    return ServerResponse.createByErrorMessage("该主管名已存在,请修改后再注册");
                }
            }
        }
    }

    /**
     * 分页获取所有的用户信息
     *
     * @param page 当前页
     * @param size 每页大小
     * @return ServerResponse<Page < User>>
     */
    @GetMapping
    @RequiredPermission("admin")
    public ServerResponse<Page<User>> getAllUserByPage(@RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        return userService.getAllUserByPage(PageRequest.of(page, size));
    }

    /**
     * 删除一个用户
     *
     * @param userId 用户Id
     * @return ServerResponse
     */
    @DeleteMapping("/{userId}")
    @RequiredPermission("admin")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteGaugeById(@PathVariable("userId") String userId) {
        if (StringUtils.isEmpty(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            try {
                Boolean delete = userService.deleteUserById(userId);
                if (delete) {
                    return ServerResponse.createBySuccessMessage("删除成功");
                } else {
                    return ServerResponse.createByErrorMessage("删除失败, 请重试");
                }
            } catch (Exception e) {
                log.error("delete user error : {}", e.getMessage());
                return ServerResponse.createByErrorMessage("删除一个用户发生未知异常");
            }
        }
    }
}
