package cn.edu.jxust.arrangeproduce.controller.portal;

import cn.edu.jxust.arrangeproduce.annotation.RequiredPermission;
import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZSS
 * @date 2019/11/30 10:01
 * @description Arrange 控制器
 */
@RestController
@RequestMapping("/arrange")
public class ArrangeController extends BaseController{

    @RequiredPermission("admin")
    @GetMapping
    public ServerResponse aspectTest(){
        System.out.println("aspectTest");
        return ServerResponse.createBySuccess();
    }

}
