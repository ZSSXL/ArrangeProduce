package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.AwArrange;
import cn.edu.jxust.arrangeproduce.repository.AwArrangeRepository;
import cn.edu.jxust.arrangeproduce.service.AwArrangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/12 9:22
 * @description awArrange 服务层接口方法实现
 */
@Slf4j
@Service
public class AwArrangeServiceImpl implements AwArrangeService {

    private final AwArrangeRepository awArrangeRepository;

    @Autowired
    public AwArrangeServiceImpl(AwArrangeRepository awArrangeRepository) {
        this.awArrangeRepository = awArrangeRepository;
    }

    @Override
    public ServerResponse createAwArrange(AwArrange awArrange) {
        awArrangeRepository.save(awArrange);
        return ServerResponse.createBySuccess();
    }

    @Override
    public String isConflict(Long awArrangeDate, String shift, String machine, String sort, String enterpriseId) {
        AwArrange awArrange = awArrangeRepository.findByArrangeDateAndShiftAndMachineAndSortAndEnterpriseId(awArrangeDate, shift, machine, sort, enterpriseId).orElse(null);
        if (awArrange != null) {
            return awArrange.getAwArrangeId();
        } else {
            return null;
        }
    }

    @Override
    public ServerResponse<Page<AwArrange>> getAllAwArrangeByEnterpriseId(String enterpriseId, Pageable pageable) {
        Page<AwArrange> awArrangePage = awArrangeRepository.findAllByEnterpriseIdOrderByCreateTimeDesc(enterpriseId, pageable);
        if (awArrangePage == null) {
            return ServerResponse.createByErrorMessage("查询失败");
        } else {
            return ServerResponse.createBySuccess(awArrangePage);
        }
    }

    @Override
    public ServerResponse<Page<AwArrange>> getAllAwArrangeByEnterpriseIdAndPush(String enterpriseId, String push, Pageable pageable) {
        Page<AwArrange> awArrangePage = awArrangeRepository.findAllByEnterpriseIdAndPushOrderByUpdateTimeDesc(enterpriseId, push, pageable);
        if (awArrangePage == null) {
            return ServerResponse.createByErrorMessage("查询失败");
        } else {
            return ServerResponse.createBySuccess(awArrangePage);
        }
    }

    @Override
    public Boolean deleteAwArrangeByAwArrangeId(String awArrangeId) {
        Integer integer = awArrangeRepository.deleteByAwArrangeId(awArrangeId);
        log.info("delete awArrange result : {}", integer);
        return integer > 0;
    }

    @Override
    public AwArrange getAwArrangeById(String awArrangeId) {
        return awArrangeRepository.findById(awArrangeId).orElse(null);
    }

    @Override
    public ServerResponse updatePush(String enterpriseId, List<String> awArrangeList) {
        for (String awArrangeId : awArrangeList) {
            Integer integer = awArrangeRepository.update(Const.PUSH, System.currentTimeMillis(), awArrangeId, enterpriseId);
            log.info("update awArrange : {} result : {}", awArrangeId, integer);
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }

    @Override
    public Boolean updateStatus(String awArrangeId, String enterpriseId) {
        Integer integer = awArrangeRepository.updateStatus(awArrangeId, enterpriseId);
        log.info("update arrange print status : {}", integer == 1);
        return integer == 1;
    }
}
