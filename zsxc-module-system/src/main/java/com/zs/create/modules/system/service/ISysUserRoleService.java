package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.SysUserRole;

import java.util.Map;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 查询所有的用户角色信息
     *
     * @return
     */
    Map<String, String> queryUserRole();

    /**
     * @Description 保存角色用户关系
     * @Author HeLiu
     * @Date 2019/9/27 14:04
     **/
    void saveRoleUser(SysUserRole sysUserRole) throws Exception;
}
