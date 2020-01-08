package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.entity.po.Employee;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.EmployeeVo;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.EmployeeService;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.*;
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
 * @date 2019/12/3 13:45
 * @description user 控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private final UserService userService;
    private final AccountService accountService;
    private final EmployeeService employeeService;
    private final TokenUtil tokenUtil;


    @Autowired
    public UserController(UserService userService, AccountService accountService, EmployeeService employeeService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.accountService = accountService;
        this.employeeService = employeeService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 主任添加员工
     *
     * @param employeeVo 注册实体
     * @param token      token
     * @param result     错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse registerUser(@Valid @RequestBody EmployeeVo employeeVo, @RequestHeader("token") String token, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            // 判断是否存在该用户，不存在则添加
            Boolean user = userService.existInDb(employeeVo.getUsername());
            if (!user) {
                String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
                String userId = UUIDUtil.getId();
                Boolean existInDn = employeeService.isExistInDn(enterpriseId, employeeVo.getEmployeeNumber());
                if (existInDn) {
                    return ServerResponse.createByErrorMessage("[" + employeeVo.getEmployeeNumber() + "] 已经存在, 请更换");
                } else {
                    try {
                        userService.createUser(User.builder()
                                .userId(userId)
                                .userName(employeeVo.getUsername())
                                .phone(employeeVo.getPhone())
                                .enterpriseId(enterpriseId)
                                .role(Const.Role.ROLE_EMPLOYEE)
                                .build());
                        employeeService.createEmployee(Employee.builder()
                                .employeeId(userId)
                                .employeeNumber(employeeVo.getEmployeeNumber())
                                .department(employeeVo.getDepartment())
                                .post(employeeVo.getPost())
                                .sex(employeeVo.getSex())
                                .enterpriseId(enterpriseId)
                                .build());
                        accountService.createAccount(Account.builder()
                                .accountId(userId)
                                .accountName(employeeVo.getUsername())
                                .password(EncryptionUtil.encrypt(employeeVo.getPassword() == null ? "123456" : employeeVo.getPassword()))
                                .build());
                        log.info("create employee : [{}] success", employeeVo.getUsername());
                        return ServerResponse.createBySuccessMessage("添加员工成功");
                    } catch (Exception e) {
                        log.error("create employee error [{}]", e.getMessage());
                        return ServerResponse.createByErrorMessage("添加员工失败");
                    }
                }
            } else {
                return ServerResponse.createByErrorMessage("该用员工已存在,请修改后再注册");
            }
        }
    }

    /**
     * 获取所有的员工信息
     *
     * @param token 用户token
     * @param page  当前页数
     * @param size  每页大小
     * @return ServerResponse<Page < User>>
     */
    @GetMapping
    @RequiredPermission
    public ServerResponse<Page<User>> getAllEmployee(@RequestHeader("token") String token
            , @RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return userService.getAllUserByRole(enterpriseId, Const.Role.ROLE_EMPLOYEE, PageRequest.of(page, size));
    }

    /**
     * 获取员工信息通过Id
     *
     * @param userId 用户Id
     * @return ServerResponse<Employee>
     */
    @GetMapping("/{userId}")
    @RequiredPermission
    public ServerResponse<EmployeeVo> getEmployeeById(@PathVariable("userId") String userId) {
        User user = userService.getUserById(userId);
        Employee emp = employeeService.getEmployeeById(userId);
        EmployeeVo employee = EmployeeVo.builder()
                .employeeId(userId)
                .username(user.getUserName())
                .phone(user.getPhone())
                .department(emp.getDepartment())
                .employeeNumber(emp.getEmployeeNumber())
                .enterpriseId(user.getEnterpriseId())
                .post(emp.getPost())
                .sex(emp.getSex())
                .build();
        return ServerResponse.createBySuccess(employee);
    }

    /**
     * 修改个人信息
     *
     * @param employeeVo 更新实体
     * @param token      token
     * @return ServerResponse
     */
    @PutMapping("/info")
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateUserInfo(@RequestBody @Valid EmployeeVo employeeVo, @RequestHeader("token") String token, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else if (StringUtils.isEmpty(employeeVo.getEmployeeId())) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            try {
                userService.createUser(User.builder()
                        .userId(employeeVo.getEmployeeId())
                        .phone(employeeVo.getPhone())
                        .enterpriseId(enterpriseId)
                        .role(Const.Role.ROLE_EMPLOYEE)
                        .userName(employeeVo.getUsername())
                        .build());
                employeeService.createEmployee(Employee.builder()
                        .sex(employeeVo.getSex())
                        .post(employeeVo.getPost())
                        .department(employeeVo.getDepartment())
                        .employeeNumber(employeeVo.getEmployeeNumber())
                        .employeeId(employeeVo.getEmployeeId())
                        .enterpriseId(enterpriseId)
                        .build());
                return ServerResponse.createBySuccessMessage("更新员工成功");
            } catch (Exception e) {
                log.error("[{}] failed to modify personal information : {}", employeeVo.getUsername(), e.getMessage());
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
    public ServerResponse updatePassword(@RequestBody String password, @RequestHeader("token") String token) {
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String tokenId = tokenUtil.getClaim(token, "tokenId").asString();
            String username = tokenUtil.getClaim(token, "username").asString();
            try {
                Boolean update = accountService.updatePassword(password, tokenId);
                if (update) {
                    return ServerResponse.createBySuccessMessage("修改密码成功");
                } else {
                    return ServerResponse.createByErrorMessage("修改密码失败");
                }
            } catch (Exception e) {
                log.error("[{}] failed to modify personal password : {}", username, e.getMessage());
                return ServerResponse.createByErrorMessage("修改个人密码发生未知异常");
            }
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户Id
     * @return ServerResponse
     */
    @DeleteMapping("/{userId}")
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteEmployee(@PathVariable("userId") String userId) {
        Boolean delete = userService.deleteUserById(userId);
        Boolean emp = employeeService.deleteEmpById(userId);
        if (delete && emp) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 打印员工信息
     *
     * @param userId 员工Id
     * @return ServerResponse<String>
     */
    @GetMapping("/print/{userId}")
    @RequiredPermission
    public ServerResponse<String> printEmp(@PathVariable("userId") String userId) {
        Employee emp = employeeService.getEmployeeById(userId);
        if (emp == null) {
            return ServerResponse.createByErrorMessage("获取二维码失败");
        } else {
            String qrCode = generateQrCode(emp.getEmployeeNumber());
            if (qrCode == null) {
                return ServerResponse.createByErrorMessage("生成二维码失败");
            } else {
                return ServerResponse.createBySuccess(emp.getEmployeeNumber(), qrCode);
            }
        }
    }

    /**
     * 生成二维码
     *
     * @return String
     */
    private String generateQrCode(String employeeNumber) {
        StringBuilder qrMessage = new StringBuilder();
        // 打码时间
        qrMessage.append("8").append(DateUtil.getDateSimple()).append("*");
        // 小拉机编号
        qrMessage.append(employeeNumber).append("*");
        // 线规
        qrMessage.append("00000").append("*");
        // 正公差
        qrMessage.append("000000").append("*");
        //负公差
        qrMessage.append("000000").append("*");
        // 任务生产时间
        qrMessage.append("00000000").append("*");
        // 早晚班： 1是早班， 0是晚班
        qrMessage.append("0").append("*");
        // 流水号 随机四位数
        qrMessage.append("0000");
        log.info("generate employee QrCode message : [{}] and the length is : [{}]", qrMessage, qrMessage.length());
        String qrCode = QrCodeUtil.createQrCode(qrMessage.toString());
        if (qrCode != null) {
            return qrCode.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\r\\n", "");
        } else {
            return null;
        }
    }
}
