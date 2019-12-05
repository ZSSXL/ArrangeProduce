package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.ArrangeVo;
import cn.edu.jxust.arrangeproduce.service.ArrangeService;
import cn.edu.jxust.arrangeproduce.util.DateUtil;
import cn.edu.jxust.arrangeproduce.util.QrCodeUtil;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author ZSS
 * @date 2019/12/4 15:13
 * @description 二维码控制器
 */
@Slf4j
@RestController
@RequestMapping("/qr")
public class QrCodeController extends BaseController {

    private final ArrangeService arrangeService;

    @Autowired
    public QrCodeController(ArrangeService arrangeService) {
        this.arrangeService = arrangeService;
    }

    /**
     * 新建排产任务并打印二维码
     *
     * @param arrangeVo 排产Vo实体
     * @param session   session
     * @param result    错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    public ServerResponse<String> printQrCode(@RequestBody @Valid ArrangeVo arrangeVo, HttpSession session, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            Boolean conflict = arrangeService.isConflict(arrangeVo.getArrangeDate(), arrangeVo.getShift(), arrangeVo.getMachine(), user.getEnterpriseId());
            String qrCode;
            if (conflict) {
                // 如果存在，直接打印
                qrCode = generateQrCode(arrangeVo);
                if (qrCode == null) {
                    return ServerResponse.createByErrorMessage("生成二维码失败");
                } else {
                    // todo 讲道理，这里打印之后也要改变数据库中的排产打印状态，但是好麻烦，但是不影响大局，后面有时间再改
                    qrCode = qrCode.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\r\\n", "");
                    return ServerResponse.createBySuccess(qrCode);
                }
            } else {
                // 如果不存在，先保存，后打印
                String arrangeId = UUIDUtil.getId();
                try {
                    arrangeService.createArrange(Arrange.builder()
                            .arrangeId(arrangeId)
                            .arrangeDate(arrangeVo.getArrangeDate())
                            .gauge(arrangeVo.getGauge())
                            .machine(arrangeVo.getMachine())
                            .shift(arrangeVo.getShift())
                            .enterpriseId(user.getEnterpriseId())
                            .weight(arrangeVo.getWeight())
                            .tolerance(arrangeVo.getTolerance())
                            .status(1)
                            .build());
                } catch (Exception e) {
                    log.error("create arrange error {}", e.getClass());
                    return ServerResponse.createByErrorMessage("新建排产任务异常");
                }
                qrCode = generateQrCode(arrangeVo);
                if (qrCode == null) {
                    return ServerResponse.createByErrorMessage("生成二维码失败");
                } else {
                    return ServerResponse.createBySuccess(qrCode);
                }
            }
        }
    }

    /**
     * 从历史记录中打印二维码
     *
     * @param arrangeId 排产Id
     * @return ServerResponse<String>
     */
    @GetMapping("/{arrangeId}")
    public ServerResponse<String> printByArrangeId(@PathVariable("arrangeId") String arrangeId, HttpSession session) {
        if (StringUtils.isEmpty(arrangeId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            Object attribute = session.getAttribute(Const.CURRENT_USER);
            if (attribute == null) {
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
                            .shift(arrange.getShift())
                            .build());
                    if (StringUtils.isEmpty(qrCode)) {
                        return ServerResponse.createByErrorMessage("生成二维码失败");
                    } else {
                        // 更新排产打印状态
                        arrange.setStatus(1);
                        ServerResponse response = arrangeService.createArrange(arrange);
                        if (response.isSuccess()) {
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
     * 生成二维码
     *
     * @return String
     */
    private String generateQrCode(ArrangeVo arrangeVo) {
        StringBuilder qrMessage = new StringBuilder();
        // 打码时间
        qrMessage.append(DateUtil.getDateSimple()).append("*");
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