package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.entity.vo.ArrangeVo;
import cn.edu.jxust.arrangeproduce.service.ArrangeService;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

    @Autowired
    public ArrangeController(ArrangeService arrangeService) {
        this.arrangeService = arrangeService;
    }

    /**
     * 新建排产任务
     *
     * @param arrangeVo 排产Vo实体
     * @param session   session
     * @param result    错误结果
     * @return ServerResponse
     */
    @PostMapping
    @RequiredPermission
    public ServerResponse createArrange(@RequestBody @Valid ArrangeVo arrangeVo, HttpSession session, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            Boolean conflict = arrangeService.isConflict(arrangeVo.getArrangeDate(), arrangeVo.getShift(), arrangeVo.getMachine(), user.getEnterpriseId());
            if (conflict) {
                return ServerResponse.createByErrorMessage("任务时间冲突，请修改生产时间或者机器");
            } else {
                String arrangeId = UUIDUtil.getId();
                try {
                    return arrangeService.createArrange(Arrange.builder()
                            .arrangeId(arrangeId)
                            .arrangeDate(arrangeVo.getArrangeDate())
                            .gauge(arrangeVo.getGauge())
                            .machine(arrangeVo.getMachine())
                            .shift(arrangeVo.getShift())
                            .enterpriseId(user.getEnterpriseId())
                            .weight(arrangeVo.getWeight())
                            .tolerance(arrangeVo.getTolerance())
                            .build());
                } catch (Exception e) {
                    log.error("create arrange error {}", e.getClass());
                    return ServerResponse.createByErrorMessage("新建排产任务异常");
                }
            }
        }
    }


}
