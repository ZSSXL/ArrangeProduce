package cn.edu.jxust.arrangeproduce.service;

import cn.edu.jxust.arrangeproduce.common.ServerResponse;
import cn.edu.jxust.arrangeproduce.entity.po.Admin;

/**
 * @author ZSS
 * @date 2019/12/5 18:17
 * @description admin 服务层接口
 */
public interface AdminService {

    /**
     * 通过名称确认是否已存在
     *
     * @param adminName 管理员名称
     * @return Boolean
     */
    Boolean existInDb(String adminName);

    /**
     * 添加管理员
     *
     * @param admin 管理员实体
     * @return ServerResponse
     */
    ServerResponse createAdmin(Admin admin);

    /**
     * 通过Id查找管理员
     *
     * @param adminId 管理员Id
     * @return Admin
     */
    Admin getAdminById(String adminId);

    /**
     * 删除管理员
     *
     * @param adminId 管理员Id
     * @return Boolean
     */
    Boolean deleteAdminById(String adminId);
}
