package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Gauge;
import cn.edu.jxust.arrangeproduce.repository.GaugeRepository;
import cn.edu.jxust.arrangeproduce.service.GaugeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/4 16:13
 * @description Gauge 服务层接口实现
 */
@Service
public class GaugeServiceImpl implements GaugeService {

    private final GaugeRepository gaugeRepository;

    @Autowired
    public GaugeServiceImpl(GaugeRepository gaugeRepository) {
        this.gaugeRepository = gaugeRepository;
    }

    @Override
    public ServerResponse createGauge(Gauge gauge) {
        Gauge save = gaugeRepository.save(gauge);
        if (save != null) {
            return ServerResponse.createBySuccessMessage("添加线规成功");
        } else {
            return ServerResponse.createByErrorMessage("添加线规失败");
        }
    }

    @Override
    public ServerResponse<List<Gauge>> getAllGaugeByEnterpriseId(String enterpriseId) {
        List<Gauge> list = gaugeRepository.findAllByEnterpriseIdOrderByGaugeDesc(enterpriseId);
        if (list != null) {
            return ServerResponse.createBySuccess(list);
        } else {
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }

    @Override
    public Boolean existInDb(String enterpriseId, BigDecimal gauge) {
        return gaugeRepository.findByEnterpriseIdAndGauge(enterpriseId, gauge).isPresent();
    }
}
