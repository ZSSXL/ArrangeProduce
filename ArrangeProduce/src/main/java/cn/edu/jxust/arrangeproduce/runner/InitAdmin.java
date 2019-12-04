package cn.edu.jxust.arrangeproduce.runner;

import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.service.UserService;
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

    private final String ADMIN = "admin";
    private final String PASS = "123456";
    private final String ROLE = "admin";

    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public InitAdmin(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Boolean exist = userService.existInDb(ADMIN);
        if (exist) {
            log.info("初始管理员已经存在");
        } else {
            String adminId = UUIDUtil.getId();
            try {
                userService.createUser(User.builder()
                        .userId(adminId)
                        .userName(ADMIN)
                        .role(ROLE)
                        .build());
                accountService.createAccount(Account.builder()
                        .accountId(adminId)
                        .accountName(ADMIN)
                        .password(EncryptionUtil.encrypt(PASS))
                        .build());
            } catch (Exception e) {
                log.error(" init admin error {}", e.getMessage());
            }
        }
    }
}
