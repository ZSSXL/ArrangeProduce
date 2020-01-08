package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.AwArrange;
import cn.edu.jxust.arrangeproduce.entity.vo.AwArrangeVo;
import cn.edu.jxust.arrangeproduce.entity.vo.UpdateVo;
import cn.edu.jxust.arrangeproduce.service.AwArrangeService;
import cn.edu.jxust.arrangeproduce.service.MachineService;
import cn.edu.jxust.arrangeproduce.util.*;
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
 * @date 2019/12/12 9:27
 * @description 退火/绕线机 排产控制器
 */
@Slf4j
@RestController
@RequestMapping("/aw")
public class AwArrangeController extends BaseController {

    private final AwArrangeService awArrangeService;
    private final TokenUtil tokenUtil;
    private final MachineService machineService;

    @Autowired
    public AwArrangeController(AwArrangeService awArrangeService, TokenUtil tokenUtil, MachineService machineService) {
        this.awArrangeService = awArrangeService;
        this.tokenUtil = tokenUtil;
        this.machineService = machineService;
    }

    /**
     * 新建退火/绕线机排产任务
     *
     * @param token       用户token
     * @param awArrangeVo 实体Vo
     * @param result      错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    public ServerResponse createAwArrange(@RequestHeader("token") String token, @RequestBody @Valid AwArrangeVo awArrangeVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            String conflict = awArrangeService.isConflict(awArrangeVo.getArrangeDate(), awArrangeVo.getShift(), awArrangeVo.getMachine(), awArrangeVo.getSort(), enterpriseId);
            if (!StringUtils.isEmpty(conflict)) {
                return ServerResponse.createByErrorMessage("任务时间冲突，请修改生产时间或者机器");
            } else {
                String awArrangeId = UUIDUtil.getId();
                String username = tokenUtil.getClaim(token, "username").asString();
                String machineName = machineService.getMachineNameByNumAndEnterpriseId(awArrangeVo.getMachine(), enterpriseId);
                if (StringUtils.isEmpty(awArrangeVo.getNegativeTolerance())) {
                    awArrangeVo.setNegativeTolerance("0.000");
                }
                if (StringUtils.isEmpty(awArrangeVo.getPositiveTolerance())) {
                    awArrangeVo.setPositiveTolerance("0.000");
                }
                try {
                    return awArrangeService.createAwArrange(AwArrange.builder()
                            .awArrangeId(awArrangeId)
                            .arrangeDate(awArrangeVo.getArrangeDate())
                            .enterpriseId(enterpriseId)
                            .gauge(awArrangeVo.getGauge())
                            .machine(awArrangeVo.getMachine())
                            .machineName(machineName)
                            .shift(awArrangeVo.getShift())
                            .weight(awArrangeVo.getWeight())
                            .status(0)
                            .sort(awArrangeVo.getSort())
                            .push(Const.DEFAULT_NO_PUSH)
                            .creator(username)
                            .negativeTolerance(awArrangeVo.getNegativeTolerance())
                            .positiveTolerance(awArrangeVo.getPositiveTolerance())
                            .inletDiameter(awArrangeVo.getInletDiameter())
                            .rawMaterials(awArrangeVo.getRawMaterials())
                            .groupNumber(awArrangeVo.getGroup())
                            .build());
                } catch (Exception e) {
                    log.error("create awArrange error {}", e.getMessage());
                    return ServerResponse.createByErrorMessage("新建退火/绕线机排产任务异常");
                }
            }
        }
    }

    /**
     * 分页查询所有的排产信息
     *
     * @param token 用户token
     * @param page  当前页
     * @param size  每页大小
     * @return ServerResponse<Page < AwArrange>>
     */
    @GetMapping
    @RequiredPermission
    public ServerResponse<Page<AwArrange>> getAllAwArrange(@RequestHeader("token") String token
            , @RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        if (StringUtils.isEmpty(token)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            return awArrangeService.getAllAwArrangeByEnterpriseId(enterpriseId, PageRequest.of(page, size));
        }
    }

    /**
     * 分页查询所有已推送的排产信息
     *
     * @param token 用户token
     * @param page  当前页
     * @param size  每页大小
     * @return ServerResponse<Page < AwArrange>>
     */
    @GetMapping("/employee")
    @RequiredPermission("employee")
    public ServerResponse<Page<AwArrange>> getAllAwArrangePushed(@RequestHeader("token") String token
            , @RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        if (StringUtils.isEmpty(token)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            return awArrangeService.getAllAwArrangeByEnterpriseIdAndPush(enterpriseId, Const.PUSH, PageRequest.of(page, size));
        }
    }

    /**
     * 批量修改
     *
     * @param token           用户token
     * @param awArrangeIdList id数组
     * @return ServerResponse
     */
    @PutMapping
    @RequiredPermission
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateAwArrangePush(@RequestHeader("token") String token, @RequestBody String[] awArrangeIdList) {
        if (awArrangeIdList == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            List<String> list = Arrays.asList(awArrangeIdList);
            try {
                return awArrangeService.updatePush(enterpriseId, list);
            } catch (Exception e) {
                log.error("An exception occurred during the update : {} ", e.getMessage());
                return ServerResponse.createByErrorMessage("更新的时候发生了未知异常，请查看日志");
            }
        }
    }

    /**
     * 删除一条排产
     *
     * @param awArrangeId 排产Id
     * @return ServerResponse
     */
    @RequiredPermission
    @DeleteMapping("/{awArrangeId}")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteAwArrangeById(@PathVariable("awArrangeId") String awArrangeId) {
        if (StringUtils.isEmpty(awArrangeId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            try {
                Boolean delete = awArrangeService.deleteAwArrangeByAwArrangeId(awArrangeId);
                if (delete) {
                    return ServerResponse.createBySuccessMessage("删除成功");
                } else {
                    return ServerResponse.createByErrorMessage("删除失败, 请重试");
                }
            } catch (Exception e) {
                log.error("delete awArrange error : {}", e.getMessage());
                return ServerResponse.createByErrorMessage("删除一条排产发生未知异常");
            }
        }
    }

    /**
     * 从历史数据中打印
     *
     * @param awArrangeId Id
     * @param token       用户token
     * @return ServerResponse<String>
     */
    @GetMapping("/{awArrangeId}")
    public ServerResponse<String> printByAwArrangeId(@PathVariable("awArrangeId") String awArrangeId, @RequestHeader("token") String token) {
        if (StringUtils.isEmpty(awArrangeId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            if (StringUtils.isEmpty(token)) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
            } else {
                AwArrange awArrange = awArrangeService.getAwArrangeById(awArrangeId);
                if (awArrange == null) {
                    return ServerResponse.createByErrorMessage("打印失败，没有该排产信息");
                } else {
                    String qrCode = generateQrCode(awArrange);
                    if (StringUtils.isEmpty(qrCode)) {
                        return ServerResponse.createByErrorMessage("生成二维码失败");
                    } else {
                        // 更新排产打印状态
                        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
                        Boolean update = awArrangeService.updateStatus(awArrangeId, enterpriseId);
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
        } else if (StringUtils.isEmpty(updateVo.getGroupNumber())) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            AwArrange awArrange = awArrangeService.getAwArrangeById(updateVo.getArrangeId());
            if (awArrange == null) {
                return ServerResponse.createByErrorMessage("更新失败，没有查询到相关的排产信息");
            } else {
                String machineNumber = machineService.getMachineByName(enterpriseId, updateVo.getMachineName());
                if (machineNumber == null) {
                    return ServerResponse.createByErrorMessage("更新失败，没有符合该设备名称的设备号");
                } else {
                    awArrange.setGauge(updateVo.getGauge());
                    awArrange.setMachine(machineNumber);
                    awArrange.setMachineName(updateVo.getMachineName());
                    awArrange.setPositiveTolerance(updateVo.getPositiveTolerance());
                    awArrange.setNegativeTolerance(updateVo.getNegativeTolerance());
                    awArrange.setInletDiameter(updateVo.getInletDiameter());
                    awArrange.setArrangeDate(updateVo.getArrangeDate());
                    awArrange.setShift(updateVo.getShift());
                    awArrange.setWeight(updateVo.getWeight());
                    awArrange.setRawMaterials(updateVo.getRawMaterials());
                    awArrange.setGroupNumber(updateVo.getGroupNumber());
                    try {
                        log.info("update aw arrange : [{}] success", updateVo.getArrangeId());
                        return awArrangeService.createAwArrange(awArrange);
                    } catch (Exception e) {
                        log.error("update aw arrange : [{}] has error : {}", updateVo.getArrangeId(), e.getMessage());
                        return ServerResponse.createByErrorMessage("修改排产信息异常");
                    }
                }
            }
        }
    }

    /**
     * 生成二维码
     *
     * @param awArrange 实体
     * @return String
     */
    private String generateQrCode(AwArrange awArrange) {
        StringBuilder qrMessage = new StringBuilder();
        // 打码时间
        qrMessage.append("8").append(DateUtil.getDateSimple()).append("*");
        // 小拉机编号
        qrMessage.append(awArrange.getMachine()).append("*");
        // 线规
        qrMessage.append(DigitUtil.formatDigit(awArrange.getGauge(), 3)).append("*");
        // 正公差
        qrMessage.append("+").append(DigitUtil.formatDigit(awArrange.getPositiveTolerance(), 3)).append("*");
        //负公差
        qrMessage.append("-").append(DigitUtil.formatDigit(awArrange.getNegativeTolerance(), 3)).append("*");
        // 任务生产时间
        qrMessage.append(DateUtil.timestampToDate(awArrange.getArrangeDate())).append("*");
        // 早晚班： 1是早班， 0是晚班
        qrMessage.append(awArrange.getShift()).append("*");
        // 流水号 随机四位数
        qrMessage.append((int) (Math.random() * 9000 + 1000));
        log.info("generate aw QrCode message : [{}] and the length is : [{}]", qrMessage, qrMessage.length());
        String qrCode = QrCodeUtil.createQrCode(qrMessage.toString());
        if (qrCode != null) {
            return qrCode.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\r\\n", "");
        } else {
            return null;
        }
    }

}
