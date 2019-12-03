package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.repository.EnterpriseRepository;
import cn.edu.jxust.arrangeproduce.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/3 16:58
 * @description Enterprise 服务层方法实现
 */
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    @Autowired
    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    public Boolean existInDbById(String enterpriseId) {
        return enterpriseRepository.findById(enterpriseId).isPresent();
    }
}
