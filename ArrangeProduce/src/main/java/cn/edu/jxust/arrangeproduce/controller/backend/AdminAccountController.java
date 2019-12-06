package cn.edu.jxust.arrangeproduce.controller.backend;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Admin;
import cn.edu.jxust.arrangeproduce.entity.vo.LoginVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.AdminService;
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
 * @date 2019/12/5 19:16
 * @description 管理员账户登录
 */
@Slf4j
@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    private final AdminService adminService;
    private final AccountService accountService;

    @Autowired
    public AdminAccountController(AdminService adminService, AccountService accountService) {
        this.adminService = adminService;
        this.accountService = accountService;
    }

    /**
     * 管理员登录
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
                Admin admin = adminService.getAdminById(login.getData());
                if (admin != null) {
                    session.setAttribute(Const.CURRENT_USER, admin);
                    return login;
                } else {
                    log.error("管理员登录失败，保存session错误");
                    return ServerResponse.createByErrorMessage("管理员登录失败, 保存session错误");
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
