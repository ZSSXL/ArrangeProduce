package cn.edu.jxust.arrangeproduce.controller.backend;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Enterprise;
import cn.edu.jxust.arrangeproduce.entity.vo.EnterpriseVo;
import cn.edu.jxust.arrangeproduce.service.*;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ZSS
 * @date 2019/12/5 9:02
 * @description 企业 控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/enterprise")
public class AdminEnterpriseController {

    private final EnterpriseService enterpriseService;
    private final UserService userService;
    private final ArrangeService arrangeService;
    private final MachineService machineService;
    private final GaugeService gaugeService;

    @Autowired
    public AdminEnterpriseController(EnterpriseService enterpriseService, UserService userService, ArrangeService arrangeService, MachineService machineService, GaugeService gaugeService) {
        this.enterpriseService = enterpriseService;
        this.userService = userService;
        this.arrangeService = arrangeService;
        this.machineService = machineService;
        this.gaugeService = gaugeService;
    }

    /**
     * 添加企业
     *
     * @param enterpriseVo 企业Vo
     * @param result       错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission("admin")
    public ServerResponse createEnterprise(@Valid @RequestBody EnterpriseVo enterpriseVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            Boolean exist = enterpriseService.existInDbByName(enterpriseVo.getEnterpriseName());
            if (exist) {
                return ServerResponse.createByErrorMessage("该企业名已存在，请更换");
            } else {
                String enterpriseId = UUIDUtil.getId();
                try {
                    return enterpriseService.createEnterprise(Enterprise.builder()
                            .enterpriseId(enterpriseId)
                            .enterpriseName(enterpriseVo.getEnterpriseName())
                            .address(enterpriseVo.getAddress())
                            .email(enterpriseVo.getEmail())
                            .build());
                } catch (Exception e) {
                    log.error("create enterprise error : {}", e.getMessage());
                    return ServerResponse.createByErrorMessage("添加企业发生未知异常");
                }
            }
        }
    }

    /**
     * 删除企业
     *
     * @param enterpriseId 企业Id
     * @return ServerResponse
     */
    @DeleteMapping("/{enterpriseId}")
    @RequiredPermission("admin")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteEnterprise(@PathVariable("enterpriseId") String enterpriseId) {
        if (StringUtils.isEmpty(enterpriseId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            try {
                gaugeService.deleteAllGaugeByEnterpriseId(enterpriseId);
                machineService.deleteAllMachineByEnterpriseId(enterpriseId);
                arrangeService.deleteAllArrangeByEnterpriseId(enterpriseId);
                userService.deleteAllUserByEnterpriseId(enterpriseId);
                enterpriseService.deleteEnterpriseId(enterpriseId);
                return ServerResponse.createByErrorMessage("删除企业失败");
            } catch (Exception e) {
                log.error("delete enterprise error : {}", e.getMessage());
                return ServerResponse.createByErrorMessage("删除企业发生未知异常");
            }
        }
    }
}
