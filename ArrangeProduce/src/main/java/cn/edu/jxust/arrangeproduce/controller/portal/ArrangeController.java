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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
                            .status(0)
                            .build());
                } catch (Exception e) {
                    log.error("create arrange error {}", e.getClass());
                    return ServerResponse.createByErrorMessage("新建排产任务异常");
                }
            }
        }
    }

    /**
     * 分页获取所有的排产信息
     *
     * @param session session
     * @param page    分页页数
     * @param size    分页大小
     * @return ServerResponse<List < Arrange>>
     */
    @GetMapping
    public ServerResponse<Page<Arrange>> getAllArrangeByEmployee(HttpSession session
            , @RequestParam(value = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page
            , @RequestParam(value = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {
            return arrangeService.getAllArrangeByEnterpriseId(user.getEnterpriseId(), PageRequest.of(page, size));
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

}
