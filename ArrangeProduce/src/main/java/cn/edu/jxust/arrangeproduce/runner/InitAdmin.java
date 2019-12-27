package cn.edu.jxust.arrangeproduce.runner;

import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.entity.po.Admin;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.AdminService;
import cn.edu.jxust.arrangeproduce.util.EncryptionUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author ZSS
 * @date 2019/12/4 11:02
 * @description 系统启动的时候创建初始管理员，没有就创建，有就算了
 */
@Slf4j
@Component
public class InitAdmin implements CommandLineRunner {

    private final static String ADMIN = "admin";
    private final static String PASS = "123456";
    private final static String ROLE = "admin";

    private final AccountService accountService;
    private final AdminService adminService;

    @Autowired
    public InitAdmin(AccountService accountService, AdminService adminService) {
        this.accountService = accountService;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {
        Boolean exist = adminService.existInDb(ADMIN);
        if (exist) {
            log.info("The initial administrator already exists");
        } else {
            String adminId = UUIDUtil.getId();
            try {
                adminService.createAdmin(Admin.builder()
                        .adminId(adminId)
                        .adminName(ADMIN)
                        .role(ROLE)
                        .build());
                accountService.createAccount(Account.builder()
                        .accountId(adminId)
                        .accountName(ADMIN)
                        .password(EncryptionUtil.encrypt(PASS))
                        .build());
            } catch (Exception e) {
                log.error("init admin error {}", e.getMessage());
            }
        }
    }
}
