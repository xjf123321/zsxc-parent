package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.SysRole;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.SysUserRole;
import com.zs.create.modules.system.mapper.SysUserRoleMapper;
import com.zs.create.modules.system.service.ISysRoleService;
import com.zs.create.modules.system.service.ISysUserRoleService;
import com.zs.create.modules.system.service.ISysUserService;
import com.zs.create.modules.workflow.assignee.entity.GroupUser;
import com.zs.create.modules.workflow.assignee.service.IAssigneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private IAssigneeService assigneeService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 查询所有用户对应的角色信息
     */
    @Override
    public Map<String, String> queryUserRole() {
        List<SysUserRole> uRoleList = this.list();
        List<SysUser> userList = userService.list();
        List<SysRole> roleList = roleService.list();
        Map<String, String> map = new IdentityHashMap<>();
        String userId = "";
        String roleId = "";
        String roleName = "";
        if (uRoleList != null && uRoleList.size() > 0) {
            for (SysUserRole uRole : uRoleList) {
                roleId = uRole.getRoleId();
                for (SysUser user : userList) {
                    userId = user.getId();
                    if (uRole.getUserId().equals(userId)) {
                        roleName = this.searchByRoleId(roleList, roleId);
                        map.put(userId, roleName);
                    }
                }
            }
            return map;
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleUser(SysUserRole sysUserRole) throws Exception {
        GroupUser gu = new GroupUser();
        gu.setGroupId(sysUserRole.getRoleId());
        gu.setUserId(sysUserRole.getUserId());
        try {
            sysUserRoleMapper.insert(sysUserRole);
            assigneeService.addGroupUser(gu);
        } catch (Exception e) {
            throw new Exception("SaveRole Error");
        }
    }

    /**
     * queryUserRole调用的方法
     *
     * @param roleList
     * @param roleId
     * @return
     */
    private String searchByRoleId(List<SysRole> roleList, String roleId) {
        while (true) {
            for (SysRole role : roleList) {
                if (roleId.equals(role.getId())) {
                    return role.getRoleName();
                }
            }
        }
    }

}
