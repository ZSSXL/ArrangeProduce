package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ResponseCode;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Group;
import cn.edu.jxust.arrangeproduce.entity.vo.GroupVo;
import cn.edu.jxust.arrangeproduce.service.GroupService;
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
 * @date 2020/1/2 20:42
 * @description 退火机收线头分组控制器
 */
@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {

    private final GroupService groupService;
    private final TokenUtil tokenUtil;

    @Autowired
    public GroupController(GroupService groupService, TokenUtil tokenUtil) {
        this.groupService = groupService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 新建收线头分组
     *
     * @param token   用户token
     * @param groupVo group Vo
     * @param result  错误结果
     * @return ServerResponse
     */
    @RequiredPermission
    @PostMapping
    public ServerResponse createGroup(@RequestHeader("token") String token, @RequestBody @Valid GroupVo groupVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
            String groupId = UUIDUtil.getId();
            Boolean hadNumber = groupService.checkGroupNumber(enterpriseId, groupVo.getGroupNumber());
            if (hadNumber) {
                return ServerResponse.createByErrorMessage("这样的分配已存在，请更改");
            } else {
                try {
                    return groupService.createGroup(Group.builder()
                            .groupId(groupId)
                            .groupNumber(groupVo.getGroupNumber())
                            .enterpriseId(enterpriseId)
                            .build());
                } catch (Exception e) {
                    log.error("create new group has error : {}", e.getMessage());
                    return ServerResponse.createByErrorMessage("新建分组发生未知异常");
                }
            }
        }
    }

    /**
     * 删除分组
     *
     * @param groupId 分组Id
     * @return ServerResponse
     */
    @RequiredPermission
    @DeleteMapping("/{groupId}")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteGroup(@PathVariable("groupId") String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            Boolean result = groupService.deleteGroupById(groupId);
            if (result) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByError();
            }
        }
    }

    /**
     * 获取所有分组
     *
     * @param token 用户token
     * @return ServerResponse<List>
     */
    @GetMapping
    @RequiredPermission
    public ServerResponse<List<Group>> getAllGroup(@RequestHeader("token") String token) {
        String enterpriseId = tokenUtil.getClaim(token, "enterpriseId").asString();
        return groupService.getAllGroupByEnterpriseId(enterpriseId);
    }
}
