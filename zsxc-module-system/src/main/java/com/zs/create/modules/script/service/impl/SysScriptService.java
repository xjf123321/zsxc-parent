package com.zs.create.modules.script.service.impl;

import com.zs.create.common.groovy.IScript;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.script.dto.ControlDefaultValueDto;
import com.zs.create.modules.system.entity.SysRole;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.service.ISysRoleService;
import com.zs.create.modules.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description 执行系统脚本Service实现
 * @Author HeLiu
 * @Date 2019/9/6 16:09
 * @Version 1.0
 **/
@Service("sysScript")
public class SysScriptService implements IScript {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * @Description 测试
     * @Author HeLiu
     * @Date 2019/9/9 9:35
     **/
    public int test(int i, int j) {
        return i + j;
    }

    /**
     * @Description 用户列表
     * @Author HeLiu
     * @Date 2019/9/9 9:59
     **/
    public List<ControlDefaultValueDto> getSysUserList() {
        List<ControlDefaultValueDto> list = new ArrayList<>();
        List<SysUser> sysUserList = sysUserService.list();
        if (sysUserList != null && sysUserList.size() > 0) {
            ControlDefaultValueDto cdvd = null;
            for (SysUser su : sysUserList) {
                cdvd = new ControlDefaultValueDto();
                cdvd.setLabel(su.getRealname());
                cdvd.setValue(su.getId());
                cdvd.setParam1(su.getUsername());
                list.add(cdvd);
            }
        }
        return list;
    }

    /**
     * @Description 当前用户
     * @Author HeLiu
     * @Date 2019/9/10 9:46
     **/
    public ControlDefaultValueDto getLoginUser() {
        //获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ControlDefaultValueDto cdvd = new ControlDefaultValueDto();
        cdvd = new ControlDefaultValueDto();
        cdvd.setLabel(loginUser.getUsername());
        cdvd.setValue(loginUser.getId());
        cdvd.setParam1(loginUser.getRealname());
        return cdvd;
    }


    /**
     * @Description 性别
     * @Author HeLiu
     * @Date 2019/9/9 10:40
     **/
    public List<ControlDefaultValueDto> getSex() {
        List<ControlDefaultValueDto> list = new ArrayList<>();
        ControlDefaultValueDto s1 = new ControlDefaultValueDto();
        s1.setLabel("男");
        s1.setValue("1");
        ControlDefaultValueDto s2 = new ControlDefaultValueDto();
        s2.setLabel("女");
        s2.setValue("0");
        list.add(s1);
        list.add(s2);
        return list;
    }

    /**
     * @Description 角色
     * @Author HeLiu
     * @Date 2019/9/9 10:43
     **/
    public List<ControlDefaultValueDto> getRole() {
        List<ControlDefaultValueDto> list = new ArrayList<>();
        List<SysRole> sysRoleList = sysRoleService.list();
        if (sysRoleList != null && sysRoleList.size() > 0) {
            ControlDefaultValueDto cdvd = null;
            for (SysRole sr : sysRoleList) {
                cdvd = new ControlDefaultValueDto();
                cdvd.setLabel(sr.getRoleName());
                cdvd.setValue(sr.getId());
                cdvd.setParam1(sr.getRoleCode());
                list.add(cdvd);
            }
        }
        return list;
    }


}
