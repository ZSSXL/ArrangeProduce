package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.entity.po.WorkLoad;

/**
 * @author ZSS
 * @date 2020/1/17 19:47
 * @description 工作量服务层接口
 */
public interface WorkLoadService {

    /**
     * 添加工作激励
     *
     * @param workLoad 工作信息
     * @return Boolean
     */
    Boolean createWorkLoad(WorkLoad workLoad);

}
