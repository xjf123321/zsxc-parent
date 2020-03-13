package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.SysRole;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-19
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * @Description 角色录入（流程角色录入）
     * @Author HeLiu
     * @Date 2019/9/27 11:13
     **/
    void saveRole(SysRole role) throws Exception;

    /**
     * @Description 角色编辑（流程角色编辑）
     * @Author HeLiu
     * @Date 2019/9/27 11:14
     **/
    void updateRoleById(SysRole role) throws Exception;
}
