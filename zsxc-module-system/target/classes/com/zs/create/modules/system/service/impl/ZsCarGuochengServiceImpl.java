package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.modules.system.entity.SysDepart;
import com.zs.create.modules.system.entity.ZsCarGuocheng;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.mapper.SysDepartMapper;
import com.zs.create.modules.system.mapper.SysUserDepartMapper;
import com.zs.create.modules.system.mapper.ZsCarGuochengMapper;
import com.zs.create.modules.system.mapper.ZsRemindRecordMapper;
import com.zs.create.modules.system.service.ZsCarGuochengService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

/**
 * <p>
 * 车辆过程表 service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsCarGuochengServiceImpl extends ServiceImpl<ZsCarGuochengMapper, ZsCarGuocheng> implements ZsCarGuochengService {

    @Autowired
    private ZsCarGuochengMapper zsCarGuochengMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private ZsRemindRecordMapper zsRemindRecordMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Override
    @Transactional
    public void add(ZsSqCar zsSqCar) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
        zsCarGuocheng.setState(zsSqCar.getStatus());
        zsCarGuocheng.setCreateTime(new Date());
        zsCarGuocheng.setApprovalOpinion(zsSqCar.getApprovalOpinion());
        zsCarGuocheng.setUserName(sysUser.getRealname());
        zsCarGuocheng.setCarId(zsSqCar.getId());
        zsCarGuocheng.setUserId(sysUser.getId());
        zsCarGuocheng.setPlayName(zsSqCar.getApprovalColumn());
        zsCarGuocheng.setAutograph(zsSqCar.getRemarks());
        zsCarGuochengMapper.insert(zsCarGuocheng);
    }

    @Override
    public void addAndTx(ZsSqCar zsSqCar) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
        zsCarGuocheng.setState(zsSqCar.getStatus());
        zsCarGuocheng.setCreateTime(new Date());
        zsCarGuocheng.setApprovalOpinion(zsSqCar.getApprovalOpinion());
        zsCarGuocheng.setUserName(sysUser.getRealname());
        zsCarGuocheng.setCarId(zsSqCar.getId());
        zsCarGuocheng.setUserId(sysUser.getId());
        zsCarGuocheng.setPlayName(zsSqCar.getApprovalColumn());
        zsCarGuocheng.setAutograph(zsSqCar.getRemarks());
        zsCarGuochengMapper.insert(zsCarGuocheng);

        //提醒信息
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        SysDepart depart = sysDepartMapper.selectById(deptId);
        try {
            WebSocketServer.SendMessage("您有一条车辆申请待审批", depart.getResponsibilityUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Transactional
    public void delete(ZsSqCar zsSqCar) {
        zsCarGuochengMapper.deleteCar(zsSqCar.getId());
    }
}
