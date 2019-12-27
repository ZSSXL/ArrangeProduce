package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
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
     * 添加机器
     *
     * @param machineVo 机器Vo实体
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
            Boolean existByName = machineService.existInDbByName(enterpriseId, machineVo.getMachineName());
            if (exist) {
                return ServerResponse.createBySuccessMessage("该机器已存在");
            } else if (existByName) {
                return ServerResponse.createByErrorMessage("已存在该机器名称");
            } else {
                String machineId = UUIDUtil.getId();
                try {
                    return machineService.createMachine(Machine.builder()
                            .machineId(machineId)
                            .machineName(machineVo.getMachineName())
                            .machineNumber(machineVo.getMachineNumber())
                            .enterpriseId(enterpriseId)
                            .sort(machineVo.getMachineSort())
                            .build());
                } catch (Exception e) {
                    log.error("create machine error : {}", e.getMessage());
                    return ServerResponse.createBySuccessMessage("添加机器发生异常错误");
                }
            }
        }
    }

    /**
     * 获取所有小拉机
     *
     * @param token token
     * @return ServerResponse<List < Machine>>
     */
    @GetMapping("/draw")
    @RequiredPermission
    public ServerResponse<List<Machine>> getAllDraw(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return machineService.getAllMachineByEnterpriseIdAndSort(enterpriseId, Const.Sort.SORT_DRAW);
    }

    /**
     * 获取所有退火机
     *
     * @param token 用户token
     * @return ServerResponse<List < Machine>>
     */
    @GetMapping("/annealing")
    @RequiredPermission
    public ServerResponse<List<Machine>> getAllAnnealing(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return machineService.getAllMachineByEnterpriseIdAndSort(enterpriseId, Const.Sort.SORT_ANNEALING);
    }

    /**
     * 获取所有绕线机
     *
     * @param token 用户token
     * @return ServerResponse<List < Machine>>
     */
    @GetMapping("/winding")
    @RequiredPermission
    public ServerResponse<List<Machine>> getAllWind(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return machineService.getAllMachineByEnterpriseIdAndSort(enterpriseId, Const.Sort.SORT_WINDING);
    }

    /**
     * 删除一个机器
     *
     * @param machineId 机器Id
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
                return ServerResponse.createByErrorMessage("删除一个机器发生未知异常");
            }
        }
    }

}
