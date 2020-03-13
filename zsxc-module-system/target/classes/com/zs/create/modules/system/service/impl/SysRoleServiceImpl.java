package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.SysRole;
import com.zs.create.modules.system.mapper.SysRoleMapper;
import com.zs.create.modules.system.service.ISysRoleService;
import com.zs.create.modules.workflow.assignee.entity.Group;
import com.zs.create.modules.workflow.assignee.service.IAssigneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-19
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private IAssigneeService assigneeService;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRole role) throws Exception {
        try {
            sysRoleMapper.insert(role);
            //流程角色添加
            Group group = new Group();
            group.setGroupId(role.getId());
            group.setGroupName(role.getRoleName());
            assigneeService.addGroup(group);
        } catch (Exception e) {
            throw new Exception("SaveRole Error");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleById(SysRole role) throws Exception {
        Group group = new Group();
        group.setGroupId(role.getId());
        group.setGroupName(role.getRoleName());
        try {
            sysRoleMapper.updateById(role);
            assigneeService.editGroup(group);
        } catch (Exception e) {
            throw new Exception("SaveRole Error");
        }
    }
}
