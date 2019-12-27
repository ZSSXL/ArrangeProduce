package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import cn.edu.jxust.arrangeproduce.entity.vo.ArrangeVo;
import cn.edu.jxust.arrangeproduce.entity.vo.UpdateVo;
import cn.edu.jxust.arrangeproduce.service.ArrangeService;
import cn.edu.jxust.arrangeproduce.service.MachineService;
import cn.edu.jxust.arrangeproduce.util.DateUtil;
import cn.edu.jxust.arrangeproduce.util.QrCodeUtil;
import cn.edu.jxust.arrangeproduce.util.TokenUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZSS
 * @date 2019/11/30 10:01
 * @description Arrange 控制器
 */
@Slf4j
@RestController
@RequestMapping("/arrange")
public class ArrangeController extends BaseController {

    private final ArrangeService arrangeService;
    private final TokenUtil tokenUtil;
    private final MachineService machineService;

    @Autowired
    public ArrangeController(ArrangeService arrangeService, TokenUtil tokenUtil, MachineService machineService) {
        this.arrangeService = arrangeService;
        this.tokenUtil = tokenUtil;
        this.machineService = machineService;
    }

    /**
     * 新建排产任务
     *
     * @param arrangeVo 排产Vo实体
     * @param token     token
     * @param result    错误结果
     */
    @PostMapping
    @RequiredPermission
    public ServerResponse createArrange(@RequestHeader("token") String token, @RequestBody @Valid ArrangeVo arrangeVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            String conflict = arrangeService.isConflict(arrangeVo.getArrangeDate(), arrangeVo.getShift(), arrangeVo.getMachine(), enterpriseId);
            if (!StringUtils.isEmpty(conflict)) {
                return ServerResponse.createByErrorMessage("任务时间冲突，请修改生产时间或者机器");
            } else {
                String username = tokenUtil.getClaim(token, "username").asString();
                String arrangeId = UUIDUtil.getId();
                String machineName = machineService.getMachineNameByNumAndEnterpriseId(arrangeVo.getMachine(), enterpriseId);
                try {
                    return arrangeService.createArrange(Arrange.builder()
                            .arrangeId(arrangeId)
                            .arrangeDate(arrangeVo.getArrangeDate())
                            .gauge(arrangeVo.getGauge())
                            .machine(arrangeVo.getMachine())
                            .machineName(machineName)
                            .shift(arrangeVo.getShift())
                            .enterpriseId(enterpriseId)
                            .weight(arrangeVo.getWeight())
                            .tolerance(arrangeVo.getTolerance())
                            .status(0)
                            .push(Const.DEFAULT_NO_PUSH)
                            .creator(username)
                            .build());
                } catch (Exception e) {
                    log.error("create arrange error {}", e.getMessage());
                    return ServerResponse.createByErrorMessage("新建排产任务异常");
                }
            }
        }
    }

    /**
     * 分页获取所有的排产信息
     *
     * @param token token
     * @param page  分页页数
     * @param size  分页大小
     * @return ServerResponse<List < Arrange>>
     */
    @GetMapping
    @RequiredPermission
    public ServerResponse<Page<Arrange>> getAllArrangeByManager(@RequestHeader("token") String token
            , @RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        if (StringUtils.isEmpty(token)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            return arrangeService.getAllArrangeByEnterpriseId(enterpriseId, PageRequest.of(page, size));
        }
    }

    /**
     * 分页获取所有的排产信息
     *
     * @param token token
     * @param page  分页页数
     * @param size  分页大小
     * @return ServerResponse<List < Arrange>>
     */
    @GetMapping("/employee")
    @RequiredPermission("employee")
    public ServerResponse<Page<Arrange>> getAllArrangeByEmployee(@RequestHeader("token") String token
            , @RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        if (StringUtils.isEmpty(token)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            return arrangeService.getAllArrangeByEnterpriseIdAndPush(enterpriseId, Const.PUSH, PageRequest.of(page, size));
        }
    }

    /**
     * 删除一条排产
     *
     * @param arrangeId 排产Id
     * @return ServerResponse
     */
    @RequiredPermission
    @DeleteMapping("/{arrangeId}")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteArrangeById(@PathVariable("arrangeId") String arrangeId) {
        if (StringUtils.isEmpty(arrangeId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            try {
                Boolean delete = arrangeService.deleteArrangeByArrangeId(arrangeId);
                if (delete) {
                    return ServerResponse.createBySuccessMessage("删除成功");
                } else {
                    return ServerResponse.createByErrorMessage("删除失败, 请重试");
                }
            } catch (Exception e) {
                log.error("delete arrange error : {}", e.getMessage());
                return ServerResponse.createByErrorMessage("删除一条排产发生未知异常");
            }
        }
    }

    /**
     * 批量修改
     *
     * @param token       用户token
     * @param arrangeList id数组
     * @return ServerResponse
     */
    @PutMapping
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateArrangePush(@RequestHeader("token") String token, @RequestBody String[] arrangeList) {
        if (arrangeList == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            List<String> list = Arrays.asList(arrangeList);
            try {
                return arrangeService.updatePush(enterpriseId, list);
            } catch (Exception e) {
                log.error("An exception occurred during the update arrange push : {} ", e.getMessage());
                return ServerResponse.createByErrorMessage("更新的时候发生了未知异常，请查看日志");
            }
        }
    }

    /**
     * 新建排产任务并打印二维码
     *
     * @param arrangeVo 排产Vo实体
     * @param token     token
     * @param result    错误结果
     * @return ServerResponse
     */
    @PostMapping("/print")
    @RequiredPermission
    public ServerResponse<String> printQrCode(@RequestBody @Valid ArrangeVo arrangeVo, @RequestHeader("token") String token, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            String conflict = arrangeService.isConflict(arrangeVo.getArrangeDate(), arrangeVo.getShift(), arrangeVo.getMachine(), enterpriseId);
            String qrCode = generateQrCode(arrangeVo);
            if (!StringUtils.isEmpty(conflict)) {
                // 如果存在，直接打印
                if (qrCode == null) {
                    return ServerResponse.createByErrorMessage("生成二维码失败");
                } else {
                    Boolean update = arrangeService.updateStatus(conflict, enterpriseId);
                    if (update) {
                        return ServerResponse.createBySuccess(qrCode);
                    } else {
                        return ServerResponse.createBySuccess("打印成功，但是更新排产信息打印状态失败", qrCode);
                    }
                }
            } else {
                // 如果不存在，先保存，后打印
                if (qrCode == null) {
                    return ServerResponse.createByErrorMessage("生成二维码失败");
                } else {
                    String username = tokenUtil.getClaim(token, "username").asString();
                    String arrangeId = UUIDUtil.getId();
                    String machineName = machineService.getMachineNameByNumAndEnterpriseId(arrangeVo.getMachine(), enterpriseId);
                    try {
                        arrangeService.createArrange(Arrange.builder()
                                .arrangeId(arrangeId)
                                .arrangeDate(arrangeVo.getArrangeDate())
                                .gauge(arrangeVo.getGauge())
                                .machine(arrangeVo.getMachine())
                                .shift(arrangeVo.getShift())
                                .enterpriseId(enterpriseId)
                                .weight(arrangeVo.getWeight())
                                .machineName(machineName)
                                .push(Const.DEFAULT_NO_PUSH)
                                .tolerance(arrangeVo.getTolerance())
                                // 更新打印状态为已打印(1)
                                .status(1)
                                .creator(username)
                                .build());
                    } catch (Exception e) {
                        log.error("create arrange error {}", e.getClass());
                        return ServerResponse.createByErrorMessage("新建排产任务异常");
                    }
                    return ServerResponse.createBySuccess(qrCode);
                }
            }
        }
    }

    /**
     * 从历史记录中打印二维码
     *
     * @param arrangeId 排产Id
     * @param token     token
     * @return ServerResponse<String>
     */
    @GetMapping("/{arrangeId}")
    public ServerResponse<String> printByArrangeId(@PathVariable("arrangeId") String arrangeId, @RequestHeader("token") String token) {
        if (StringUtils.isEmpty(arrangeId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            if (StringUtils.isEmpty(token)) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
            } else {
                Arrange arrange = arrangeService.getArrangeById(arrangeId);
                if (arrange == null) {
                    return ServerResponse.createByErrorMessage("打印失败，没有该排产信息");
                } else {
                    String qrCode = generateQrCode(ArrangeVo.builder()
                            .machine(arrange.getMachine())
                            .gauge(arrange.getGauge())
                            .tolerance(arrange.getTolerance())
                            .arrangeDate(arrange.getArrangeDate())
                            .shift(arrange.getShift())
                            .build());
                    if (StringUtils.isEmpty(qrCode)) {
                        return ServerResponse.createByErrorMessage("生成二维码失败");
                    } else {
                        // 更新排产打印状态
                        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
                        Boolean update = arrangeService.updateStatus(arrangeId, enterpriseId);
                        if (update) {
                            return ServerResponse.createBySuccess(qrCode);
                        } else {
                            return ServerResponse.createBySuccess("打印成功，但是更新排产信息打印状态失败", qrCode);
                        }

                    }
                }
            }
        }
    }

    /**
     * 修改排场信息
     *
     * @param token    用户token
     * @param updateVo 更新实体Vo
     * @param result   错误结果
     * @return ServerResponse
     */
    @PutMapping("/update")
    @RequiredPermission
    public ServerResponse updateArrange(@RequestHeader("token") String token, @RequestBody @Valid UpdateVo updateVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            Arrange arrange = arrangeService.getArrangeById(updateVo.getArrangeId());
            if (arrange == null) {
                return ServerResponse.createByErrorMessage("更新失败，没有查询到相关的排产信息");
            } else {
                String machineNumber = machineService.getMachineByName(enterpriseId, updateVo.getMachineName());
                if (machineNumber == null) {
                    return ServerResponse.createByErrorMessage("更新失败，没有符合该设备名称的设备号");
                } else {
                    arrange.setArrangeDate(updateVo.getArrangeDate());
                    arrange.setGauge(updateVo.getGauge());
                    arrange.setMachine(machineNumber);
                    arrange.setMachineName(updateVo.getMachineName());
                    arrange.setTolerance(updateVo.getTolerance());
                    arrange.setShift(updateVo.getShift());
                    arrange.setWeight(updateVo.getWeight());
                    try {
                        log.info("update arrange : {} success", updateVo.getArrangeId());
                        return arrangeService.createArrange(arrange);
                    } catch (Exception e) {
                        log.error("modify arrange : {} has error : {}", updateVo.getArrangeId(), e.getMessage());
                        return ServerResponse.createByErrorMessage("修改排产信息异常");
                    }
                }
            }
        }
    }

    /**
     * 生成二维码
     *
     * @return String
     */
    private String generateQrCode(ArrangeVo arrangeVo) {
        StringBuilder qrMessage = new StringBuilder();
        // 打码时间
        qrMessage.append("8").append(DateUtil.getDateSimple()).append("*");
        // 小拉机编号
        qrMessage.append(arrangeVo.getMachine()).append("*");
        // 线规
        qrMessage.append(arrangeVo.getGauge()).append("*");
        // 公差
        qrMessage.append(arrangeVo.getTolerance()).append("*");
        // 任务生产时间
        qrMessage.append(DateUtil.timestampToDate(arrangeVo.getArrangeDate())).append("*");
        // 早晚班： 1是早班， 0是晚班
        qrMessage.append(arrangeVo.getShift()).append("*");
        // 流水号 随机四位数
        qrMessage.append((int) (Math.random() * 9000 + 1000));
        log.info("generate QrCode message : {}", qrMessage);
        String qrCode = QrCodeUtil.createQrCode(qrMessage.toString());
        if (qrCode != null) {
            return qrCode.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\r\\n", "");
        } else {
            return null;
        }
    }


}
