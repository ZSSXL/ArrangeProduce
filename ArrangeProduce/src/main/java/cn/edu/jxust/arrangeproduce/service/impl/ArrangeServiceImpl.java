package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import cn.edu.jxust.arrangeproduce.repository.ArrangeRepository;
import cn.edu.jxust.arrangeproduce.service.ArrangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZSS
 * @date 2019/11/30 9:58
 * @description Arrange 服务层实现方法
 */
@Slf4j
@Service
public class ArrangeServiceImpl implements ArrangeService {

    private final ArrangeRepository arrangeRepository;

    @Autowired
    public ArrangeServiceImpl(ArrangeRepository arrangeRepository) {
        this.arrangeRepository = arrangeRepository;
    }

    @Override
    public ServerResponse createArrange(Arrange arrange) {
        arrangeRepository.save(arrange);
        return ServerResponse.createBySuccess();
    }

    @Override
    public String isConflict(Long arrangeDate, String shift, String machine, String enterpriseId) {
        Arrange arrange = arrangeRepository.findByArrangeDateAndShiftAndMachineAndEnterpriseId(arrangeDate, shift, machine, enterpriseId).orElse(null);
        if (arrange != null) {
            return arrange.getArrangeId();
        } else {
            return null;
        }
    }

    @Override
    public Boolean deleteAllArrangeByEnterpriseId(String enterpriseId) {
        Integer integer = arrangeRepository.deleteAllByEnterpriseId(enterpriseId);
        log.info("delete all arrange result : {}", integer);
        return integer > 0;
    }

    @Override
    public Boolean deleteArrangeByArrangeId(String arrangeId) {
        Integer integer = arrangeRepository.deleteByArrangeId(arrangeId);
        log.info("delete arrange result : {}", integer);
        return integer > 0;
    }

    @Override
    public Arrange getArrangeById(String arrangeId) {
        return arrangeRepository.findById(arrangeId).orElse(null);
    }

    @Override
    public ServerResponse<Page<Arrange>> getAllArrangeByEnterpriseId(String enterpriseId, Pageable pageable) {
        Page<Arrange> arrangeList = arrangeRepository.findAllByEnterpriseIdOrderByCreateTimeDesc(enterpriseId, pageable);
        if (arrangeList == null) {
            return ServerResponse.createByErrorMessage("查询信息失败");
        } else {
            return ServerResponse.createBySuccess(arrangeList);
        }
    }

    @Override
    public ServerResponse<Page<Arrange>> getAllArrangeByEnterpriseIdAndPush(String enterpriseId, String push, Pageable pageable) {
        Page<Arrange> arrangeList = arrangeRepository.findAllByEnterpriseIdAndPushOrderByCreateTimeDesc(enterpriseId, push, pageable);
        if (arrangeList == null) {
            return ServerResponse.createByErrorMessage("查询信息失败");
        } else {
            return ServerResponse.createBySuccess(arrangeList);
        }
    }

    @Override
    public ServerResponse updatePush(String enterpriseId, List<String> arrangeIdList) {
        for (String arrangeId : arrangeIdList) {
            Integer integer = arrangeRepository.update(Const.PUSH, arrangeId, enterpriseId);
            log.info("update arrange : {} result : {}", arrangeId, integer);
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }

    @Override
    public Boolean updateStatus(String arrangeId, String enterpriseId) {
        Integer integer = arrangeRepository.updateStatus(arrangeId, enterpriseId);
        log.info("update arrange print status : {}", integer == 1);
        return integer == 1;
    }
}
