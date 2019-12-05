package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Enterprise;
import cn.edu.jxust.arrangeproduce.repository.EnterpriseRepository;
import cn.edu.jxust.arrangeproduce.service.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/12/3 16:58
 * @description Enterprise 服务层方法实现
 */
@Slf4j
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    @Autowired
    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    public ServerResponse createEnterprise(Enterprise enterprise) {
        Enterprise save = enterpriseRepository.save(enterprise);
        if (save != null) {
            return ServerResponse.createBySuccessMessage("添加企业成功");
        } else {
            return ServerResponse.createByErrorMessage("添加企业失败");
        }
    }

    @Override
    public Boolean existInDbById(String enterpriseId) {
        return enterpriseRepository.findById(enterpriseId).isPresent();
    }

    @Override
    public Boolean existInDbByName(String enterpriseName) {
        return enterpriseRepository.findByEnterpriseName(enterpriseName).isPresent();
    }

    @Override
    public Boolean deleteEnterpriseId(String enterpriseId) {
        Integer integer = enterpriseRepository.deleteByEnterpriseId(enterpriseId);
        log.info("delete enterprise result : {}", integer);
        return integer > 0;
    }
}
