package cn.edu.jxust.arrangeproduce.controller;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZSS
 * @date 2019/11/30 10:01
 * @description Arrange 控制器
 */
@RestController
@RequestMapping("/arrange")
public class ArrangeController {

    @PostMapping
    public ServerResponse test(){
        return ServerResponse.createBySuccessMessage("连通成功");
    }

}
