package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Gauge;
import cn.edu.jxust.arrangeproduce.service.GaugeService;
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/4 16:17
 * @description 线规控制器
 */
@Slf4j
@RestController
@RequestMapping("/gauge")
public class GaugeController extends BaseController {

    private final GaugeService gaugeService;
    private final TokenUtil tokenUtil;

    @Autowired
    public GaugeController(GaugeService gaugeService, TokenUtil tokenUtil) {
        this.gaugeService = gaugeService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 添加线规
     *
     * @param gauge  线规
     * @param token  token
     * @param result 错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    public ServerResponse createGauge(@RequestBody @NotEmpty String gauge, @RequestHeader("token") String token, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            BigDecimal gaugeDecimal = new BigDecimal(gauge);
            Boolean exist = gaugeService.existInDb(enterpriseId, gaugeDecimal);
            if (exist) {
                return ServerResponse.createByErrorMessage("改线规已存在，请仔细查找");
            } else {
                String gaugeId = UUIDUtil.getId();
                try {
                    return gaugeService.createGauge(Gauge.builder()
                            .gaugeId(gaugeId)
                            .gauge(gaugeDecimal)
                            .enterpriseId(enterpriseId)
                            .build());
                } catch (Exception e) {
                    log.error("create gauge error : {}", e.getMessage());
                    return ServerResponse.createByErrorMessage("添加线规发生异常错误");
                }
            }
        }
    }

    /**
     * 查询所有的线规
     *
     * @param token token
     * @return ServerResponse<List < Gauge>>
     */
    @GetMapping
    @RequiredPermission
    public ServerResponse<List<Gauge>> getAllGauge(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return gaugeService.getAllGaugeByEnterpriseId(enterpriseId);
    }

    /**
     * 删除一条线规
     *
     * @param gaugeId 线规Id
     * @return ServerResponse
     */
    @RequiredPermission
    @DeleteMapping("/{gaugeId}")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteGaugeById(@PathVariable("gaugeId") String gaugeId) {
        if (StringUtils.isEmpty(gaugeId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            try {
                Boolean delete = gaugeService.deleteGaugeByGaugeId(gaugeId);
                if (delete) {
                    return ServerResponse.createBySuccessMessage("删除成功");
                } else {
                    return ServerResponse.createByErrorMessage("删除失败, 请重试");
                }
            } catch (Exception e) {
                log.error("delete gauge error : {}", e.getMessage());
                return ServerResponse.createByErrorMessage("删除一条线规发生未知异常");
            }
        }
    }

}
