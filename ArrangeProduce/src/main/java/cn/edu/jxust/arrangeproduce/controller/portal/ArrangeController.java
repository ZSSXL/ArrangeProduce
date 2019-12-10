package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.Const;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Arrange;
import cn.edu.jxust.arrangeproduce.entity.vo.ArrangeVo;
import cn.edu.jxust.arrangeproduce.service.ArrangeService;
import cn.edu.jxust.arrangeproduce.util.RedisPoolUtil;
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

    @Autowired
    public ArrangeController(ArrangeService arrangeService, TokenUtil tokenUtil) {
        this.arrangeService = arrangeService;
        this.tokenUtil = tokenUtil;
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
            Boolean conflict = arrangeService.isConflict(arrangeVo.getArrangeDate(), arrangeVo.getShift(), arrangeVo.getMachine(), enterpriseId);
            if (conflict) {
                return ServerResponse.createByErrorMessage("任务时间冲突，请修改生产时间或者机器");
            } else {
                String arrangeId = UUIDUtil.getId();
                try {
                    arrangeService.createArrange(Arrange.builder()
                            .arrangeId(arrangeId)
                            .arrangeDate(arrangeVo.getArrangeDate())
                            .gauge(arrangeVo.getGauge())
                            .machine(arrangeVo.getMachine())
                            .shift(arrangeVo.getShift())
                            .enterpriseId(enterpriseId)
                            .weight(arrangeVo.getWeight())
                            .tolerance(arrangeVo.getTolerance())
                            .status(0)
                            .creator(enterpriseId)
                            .build());
                } catch (Exception e) {
                    log.error("create arrange error {}", e.getMessage());
                    return ServerResponse.createByErrorMessage("新建排产任务异常");
                }
                try {
                    return ServerResponse.createBySuccess();
                } catch (Exception e) {
                    log.error("notice message has error : {}", e.getMessage());
                    return ServerResponse.createByError();
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
    public ServerResponse<Page<Arrange>> getAllArrangeByEmployee(@RequestHeader("token") String token
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
     * 员工上线后查看是否有未读消息
     *
     * @param token token
     * @return ServeResponse
     */
    @GetMapping("/message")
    @RequiredPermission("employee")
    public ServerResponse<String> getMessage(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        String result = RedisPoolUtil.get(enterpriseId + "Arrange");
        if (StringUtils.isEmpty(result)) {
            return ServerResponse.createByErrorMessage("没有信息新消息");
        } else {
            return ServerResponse.createBySuccess("有新消息", result);
        }
    }

    /**
     * 员工查确认了消息后，删除Redis中的记录
     *
     * @param token token
     * @return ServerResponse
     */
    @DeleteMapping("/message")
    @RequiredPermission("employee")
    public ServerResponse delMessage(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        Long del = RedisPoolUtil.del(enterpriseId + "Arrange");
        if (del == 1) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("没有什么好删除的了");
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
