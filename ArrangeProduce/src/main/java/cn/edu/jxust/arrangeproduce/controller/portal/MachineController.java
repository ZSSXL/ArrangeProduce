package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Machine;
import cn.edu.jxust.arrangeproduce.entity.vo.MachineVo;
import cn.edu.jxust.arrangeproduce.service.MachineService;
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ZSS
 * @date 2019/12/4 16:36
 * @description Machine 控制器
 */
@Slf4j
@RestController
@RequestMapping("/machine")
public class MachineController extends BaseController {

    private final MachineService machineService;
    private final TokenUtil tokenUtil;

    @Autowired
    public MachineController(MachineService machineService, TokenUtil tokenUtil) {
        this.machineService = machineService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 添加小拉机
     *
     * @param machineVo 小拉机Vo实体
     * @param token     token
     * @param result    错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    public ServerResponse createMachine(@RequestBody @Valid MachineVo machineVo, @RequestHeader("token") String token, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            Boolean exist = machineService.existInDb(enterpriseId, machineVo.getMachineNumber());
            if (exist) {
                return ServerResponse.createBySuccessMessage("该小拉机已添加");
            } else {
                String machineId = UUIDUtil.getId();
                try {
                    return machineService.createMachine(Machine.builder()
                            .machineId(machineId)
                            .machineName(machineVo.getMachineName())
                            .machineNumber(machineVo.getMachineNumber())
                            .enterpriseId(enterpriseId)
                            .build());
                } catch (Exception e) {
                    log.error("create machine error : {}", e.getMessage());
                    return ServerResponse.createBySuccessMessage("添加小拉机发生异常错误");
                }
            }
        }
    }

    /**
     * 获取所有的小拉机信息
     *
     * @param token token
     * @return ServerResponse<List < Machine>>
     */
    @GetMapping
    @RequiredPermission
    public ServerResponse<List<Machine>> getAllMachine(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return machineService.getAllMachineByEnterpriseId(enterpriseId);
    }

    /**
     * 删除个小拉机
     *
     * @param machineId 小拉机Id
     * @return ServerResponse
     */
    @RequiredPermission
    @DeleteMapping("/{machineId}")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteGaugeById(@PathVariable("machineId") String machineId) {
        if (StringUtils.isEmpty(machineId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            try {
                Boolean delete = machineService.deleteMachineByMachineId(machineId);
                if (delete) {
                    return ServerResponse.createBySuccessMessage("删除成功");
                } else {
                    return ServerResponse.createByErrorMessage("删除失败, 请重试");
                }
            } catch (Exception e) {
                log.error("delete machine error : {}", e.getMessage());
                return ServerResponse.createByErrorMessage("删除一个小拉机发生未知异常");
            }
        }
    }

}
