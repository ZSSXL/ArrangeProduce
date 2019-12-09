package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.repository.AccountRepository;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import cn.edu.jxust.arrangeproduce.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/3 11:30
 * @description Account 服务层实现方法
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ServerResponse createAccount(Account account) {
        accountRepository.save(account);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> login(String username, String password) {
        Account account = accountRepository.findByAccountNameAndPassword(username, password).orElse(null);
        if (account != null) {
            log.info("{} tried to login success when : {}", username, DateUtil.getDateComplete());
            return ServerResponse.createBySuccess("登录成功", account.getAccountId());
        } else {
            log.info("{} tried to login but failed at : {}", username, DateUtil.getDateComplete());
            return ServerResponse.createByErrorMessage("登录失败, 用户名或密码错误");
        }
    }
}
