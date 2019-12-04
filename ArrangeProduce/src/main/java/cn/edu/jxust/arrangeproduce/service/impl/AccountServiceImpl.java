package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Account;
import cn.edu.jxust.arrangeproduce.repository.AccountRepository;
import cn.edu.jxust.arrangeproduce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/3 11:30
 * @description Account 服务层实现方法
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ServerResponse createAccount(Account account) {
        Account save = accountRepository.save(account);
        if (save == null) {
            return ServerResponse.createByError();
        } else {
            return ServerResponse.createBySuccess();
        }
    }

    @Override
    public String login(String username, String password) {
        Account account = accountRepository.findByAccountNameAndPassword(username, password).orElse(null);
        if (account != null) {
            return account.getAccountId();
        } else {
            return null;
        }
    }
}
