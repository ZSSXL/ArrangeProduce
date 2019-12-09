package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Admin;
import cn.edu.jxust.arrangeproduce.repository.AdminRepository;
import cn.edu.jxust.arrangeproduce.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/5 18:17
 * @description admin 服务层方法实现
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Boolean existInDb(String adminName) {
        return adminRepository.findByAdminName(adminName).isPresent();
    }

    @Override
    public ServerResponse createAdmin(Admin admin) {
        adminRepository.save(admin);
        log.info("create admin : {} success", admin.getAdminName());
        return ServerResponse.createBySuccessMessage("添加管理员成功");
    }

    @Override
    public Admin getAdminById(String adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

    @Override
    public Boolean deleteAdminById(String adminId) {
        return null;
    }
}
