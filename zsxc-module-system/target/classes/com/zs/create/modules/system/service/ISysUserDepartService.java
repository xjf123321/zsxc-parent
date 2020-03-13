package com.zs.create.modules.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.SysUserDepart;
import com.zs.create.modules.system.model.DepartIdModel;

import java.util.List;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 *
 * @Author ZhiLin
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {


    /**
     * 根据指定用户id查询部门信息
     *
     * @param userId
     * @return
     */
    List<DepartIdModel> queryDepartIdsOfUser(String userId);


    /**
     * 根据部门id查询用户信息
     *
     * @param depId
     * @return
     */
    List<SysUser> queryUserByDepId(String depId);
}
