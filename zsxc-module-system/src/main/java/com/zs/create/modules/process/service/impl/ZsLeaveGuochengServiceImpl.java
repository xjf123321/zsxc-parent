package com.zs.create.modules.process.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import com.zs.create.modules.process.mapper.ZsLeaveGuochengMapper;
import com.zs.create.modules.process.service.ZsLeaveGuochengService;
import com.zs.create.modules.system.entity.SysDepart;
import com.zs.create.modules.system.entity.ZsRemindRecord;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.mapper.SysDepartMapper;
import com.zs.create.modules.system.mapper.SysUserDepartMapper;
import com.zs.create.modules.system.mapper.ZsRemindRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class ZsLeaveGuochengServiceImpl extends ServiceImpl<ZsLeaveGuochengMapper, ZsLeaveGuocheng> implements ZsLeaveGuochengService {

    @Autowired
    private ZsLeaveGuochengMapper zsLeaveGuochengMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private ZsRemindRecordMapper zsRemindRecordMapper;

    @Override
    @Transactional
    public void add(ZsSqLeave zsSqLeave) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
        zsLeaveGuocheng.setState(zsSqLeave.getStatus());
        zsLeaveGuocheng.setCreateTime(new Date());
        zsLeaveGuocheng.setApprovalOpinion(zsSqLeave.getApprovalOpinion());
        zsLeaveGuocheng.setUserName(sysUser.getRealname());
        zsLeaveGuocheng.setPlayName(zsSqLeave.getApprovalColumn());
        zsLeaveGuocheng.setLeaveId(zsSqLeave.getId());
        zsLeaveGuocheng.setUserId(sysUser.getId());
        zsLeaveGuocheng.setAutograph(zsSqLeave.getRemarks());
        zsLeaveGuochengMapper.insert(zsLeaveGuocheng);
    }

    @Override
    @Transactional
    public void addAndTx(ZsSqLeave zsSqLeave) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
        zsLeaveGuocheng.setState(zsSqLeave.getStatus());
        zsLeaveGuocheng.setCreateTime(new Date());
        zsLeaveGuocheng.setApprovalOpinion(zsSqLeave.getApprovalOpinion());
        zsLeaveGuocheng.setUserName(sysUser.getRealname());
        zsLeaveGuocheng.setPlayName(zsSqLeave.getApprovalColumn());
        zsLeaveGuocheng.setLeaveId(zsSqLeave.getId());
        zsLeaveGuocheng.setUserId(sysUser.getId());
        zsLeaveGuocheng.setAutograph(zsSqLeave.getRemarks());
        zsLeaveGuochengMapper.insert(zsLeaveGuocheng);

        //提醒信息
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        SysDepart depart = sysDepartMapper.selectById(deptId);
        try {
            WebSocketServer.SendMessage("您有一条请假申请待审批", depart.getResponsibilityUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
