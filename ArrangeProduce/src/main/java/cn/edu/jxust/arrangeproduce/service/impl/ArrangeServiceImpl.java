package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import cn.edu.jxust.arrangeproduce.repository.ArrangeRepository;
import cn.edu.jxust.arrangeproduce.service.ArrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2019/11/30 9:58
 * @description Arrange 服务层实现方法
 */
@Service
public class ArrangeServiceImpl implements ArrangeService {

    private final ArrangeRepository arrangeRepository;

    @Autowired
    public ArrangeServiceImpl(ArrangeRepository arrangeRepository) {
        this.arrangeRepository = arrangeRepository;
    }

    @Override
    public ServerResponse createArrange(Arrange arrange) {
        Arrange save = arrangeRepository.save(arrange);
        if (save != null) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public Boolean isConflict(Long arrangeDate, String shift, String machine, String enterpriseId) {
        return arrangeRepository.findByArrangeDateAndShiftAndMachineAndEnterpriseId(arrangeDate, shift, machine, enterpriseId).isPresent();
    }
}
