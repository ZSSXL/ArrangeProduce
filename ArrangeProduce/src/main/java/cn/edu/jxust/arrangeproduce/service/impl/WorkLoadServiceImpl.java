package cn.edu.jxust.arrangeproduce.service.impl;

import cn.edu.jxust.arrangeproduce.entity.po.WorkLoad;
import cn.edu.jxust.arrangeproduce.repository.WorkLoadRepository;
import cn.edu.jxust.arrangeproduce.service.WorkLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZSS
 * @date 2020/1/17 19:47
 * @description 工作量接口方法实现
 */
@Service
public class WorkLoadServiceImpl implements WorkLoadService {

    private final WorkLoadRepository workLoadRepository;

    @Autowired
    public WorkLoadServiceImpl(WorkLoadRepository workLoadRepository) {
        this.workLoadRepository = workLoadRepository;
    }


    @Override
    public Boolean createWorkLoad(WorkLoad workLoad) {
        workLoadRepository.save(workLoad);
        return true;
    }
}
