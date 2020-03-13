package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.system.vo.SysUserCacheInfo;
import com.zs.create.modules.system.entity.FamilyLawRecords;
import com.zs.create.modules.system.entity.LawRecords;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.ZsOperationalSituation;
import com.zs.create.modules.workflow.vo.WorkflowUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

    public SysUser getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    public void addUserWithRole(SysUser user, String roles);


    /**
     * 修改用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    public void editUserWithRole(SysUser user, String roles) throws Exception;

    /**
     * 获取用户的授权角色
     *
     * @param username
     * @return
     */
    public List<String> getRole(String username);

    /**
     * 查询用户信息包括 部门信息
     *
     * @param username
     * @return
     */
    public SysUserCacheInfo getCacheUser(String username);

    /**
     * 根据部门Id查询
     *
     * @param
     * @return
     */
    public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

    /**
     * 根据角色Id查询
     *
     * @param
     * @return
     */
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param orgCode
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    public SysUser getUserByPhone(String phone);


    /**
     * 根据邮箱获取用户
     */
    public SysUser getUserByEmail(String email);


    /**
     * 添加用户和用户部门关系
     *
     * @param user
     * @param selectedParts
     */
    void addUserWithDepart(SysUser user, String selectedParts);

    /**
     * 编辑用户和用户部门关系
     *
     * @param user
     * @param departs
     */
    void editUserWithDepart(SysUser user, String departs);

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);

    Map<String, Object> queryCandidatePersonal(WorkflowUser workflowUser, Integer pageNo, Integer pageSize);

    void addUserLaw(SysUser user, LawRecords lawRecords);

    void addUserFamilyLaw(SysUser user, FamilyLawRecords familyLawRecords);

    Map selectByUserId(String id);

    SysUser selectInformation();


}
