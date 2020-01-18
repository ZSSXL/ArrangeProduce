package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.WorkLoad;
import cn.edu.jxust.arrangeproduce.service.WorkLoadService;
import cn.edu.jxust.arrangeproduce.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZSS
 * @date 2020/1/17 19:50
 * @description 工作量控制层
 * 注意：现在这个计算工作量有一个重大Bug，手持打码机上传工作量，存到数据库要分员工存储
 * 但是，手持打码器没有token，所有就没有enterpriseId(企业id)
 * 那么问题来了，当查看某个员工的工作量时候，如1001的，那么会把其他企业的员工编号是1001的员工的工作量也查到
 * 当然，现在这个平台只有一个企业，该问题不会出现，但是等有第二家企业加盟，这个问题就必须解决
 * 目前有两个方法可以解决这个问题：
 * 一：员工二维码上带enterpriseId（我推荐这个方法, 但是需要手持打码机配合）
 * 二：手持打码机上做用户登录的功能，这样就会有token
 */
@Slf4j
@RestController
@RequestMapping("/work")
public class WorkLoadController extends BaseController {

    private final WorkLoadService workLoadService;

    @Autowired
    public WorkLoadController(WorkLoadService workLoadService) {
        this.workLoadService = workLoadService;
    }

    /**
     * 添加工作量
     *
     * @param data 相关数据
     * @return ServerResponse
     */
    @GetMapping
    public ServerResponse createWork(String data) {
        if (StringUtils.isEmpty(data)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            log.info("Get data from api : [{}] and the length is : [{}]", data, data.length());
            String[] result = data.split("\\*");
            String workId = UUIDUtil.getId();
            try {
                workLoadService.createWorkLoad(WorkLoad.builder()
                        .workId(workId)
                        .employeeNumber("1" + result[7])
                        .data(data)
                        .build());
                return ServerResponse.createBySuccessMessage("保存成功");
            } catch (Exception e) {
                log.error("添加工作量发生未知异常：[{}]", e.getMessage());
                return ServerResponse.createByErrorMessage("添加工作量发生未知异常");
            }
        }
    }
}
