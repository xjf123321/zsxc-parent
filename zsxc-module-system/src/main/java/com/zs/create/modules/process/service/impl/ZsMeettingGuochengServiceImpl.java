package com.zs.create.modules.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.modules.process.entity.ZsMeettingGuocheng;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.process.mapper.ZsMeettingGuochengMapper;
import com.zs.create.modules.process.service.ZsMeettingGuochengService;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 会议申请记录
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-4
 */
@Service
@Slf4j
public class ZsMeettingGuochengServiceImpl extends ServiceImpl<ZsMeettingGuochengMapper, ZsMeettingGuocheng> implements ZsMeettingGuochengService {

   @Autowired
    private ZsMeettingGuochengMapper zsMeettingGuochengMapper;

   @Autowired
   private SysUserDepartMapper sysUserDepartMapper;

   @Autowired
   private SysDepartMapper sysDepartMapper;

   @Autowired
    ZsRemindRecordMapper zsRemindRecordMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    @Transactional
    public void add(ZsMeettingGuocheng zsMeettingProcess, String emergencyLevel) {
        zsMeettingGuochengMapper.insert(zsMeettingProcess);

        //提醒信息表插数据
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        List<String> usersId = sysUserDepartMapper.selectIdsByDeptId(deptId);
        for (String id: usersId) {
            try  {
                //PC端提醒
                SysAnnouncement sysAnnouncement = new SysAnnouncement();
                sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                sysAnnouncement.setUserIds(id + ",");    //指定用户
                sysAnnouncement.setTitile("您有一条会议申请待审批");
                sysAnnouncement.setSendTime(new Date());        //发布时间
                sysAnnouncement.setPriority(emergencyLevel);     //紧急程度
                sysAnnouncement.setMsgType("USER");             //指定用户
                sysAnnouncement.setMsgCategory("2");
                sysAnnouncementService.saveAnnouncement(sysAnnouncement);

                SysUser receiver = sysUserMapper.selectById(id);
                RTXConfig.sendNotify(receiver.getActivitiSync(), "会议申请", "您有一条会议申请待审批");

                //移动端提醒
                WebSocketServer.SendMessage("您有一条会议申请待审批", id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    @Transactional
    public void addProcess(ZsSqRoom zsSqRoom) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsMeettingGuocheng zsMeettingGuocheng = new ZsMeettingGuocheng();
        zsMeettingGuocheng.setState(zsSqRoom.getStatus());
        zsMeettingGuocheng.setCreateTime(new Date());
        zsMeettingGuocheng.setApprovalOpinion(zsSqRoom.getApprovalOpinion());
        zsMeettingGuocheng.setUserName(sysUser.getRealname());
        zsMeettingGuocheng.setUserId(sysUser.getId());
        zsMeettingGuocheng.setMeettingId(zsSqRoom.getId());
        zsMeettingGuocheng.setPlayName(zsSqRoom.getApprovalColumn());
        zsMeettingGuocheng.setAutograph(zsSqRoom.getRemarks());
        zsMeettingGuochengMapper.insert(zsMeettingGuocheng);
    }

    @Override
    public void addProcessAndTx(ZsSqRoom zsSqRoom, String emergencyLevel) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsMeettingGuocheng zsMeettingGuocheng = new ZsMeettingGuocheng();
        zsMeettingGuocheng.setState(zsSqRoom.getStatus());
        zsMeettingGuocheng.setCreateTime(new Date());
        zsMeettingGuocheng.setApprovalOpinion(zsSqRoom.getApprovalOpinion());
        zsMeettingGuocheng.setUserName(sysUser.getRealname());
        zsMeettingGuocheng.setUserId(sysUser.getId());
        zsMeettingGuocheng.setMeettingId(zsSqRoom.getId());
        zsMeettingGuocheng.setPlayName(zsSqRoom.getApprovalColumn());
        zsMeettingGuocheng.setAutograph(zsSqRoom.getRemarks());
        zsMeettingGuochengMapper.insert(zsMeettingGuocheng);

        //提醒信息表插数据
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        List<String> usersId = sysUserDepartMapper.selectByDeptId(deptId);
        for (String id: usersId) {
            try {
                //PC端提醒
                SysAnnouncement sysAnnouncement = new SysAnnouncement();
                sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                sysAnnouncement.setUserIds(id + ",");    //指定用户
                sysAnnouncement.setTitile("您有一条会议申请待审批");
                sysAnnouncement.setSendTime(new Date());        //发布时间
                sysAnnouncement.setPriority(emergencyLevel);     //紧急程度
                sysAnnouncement.setMsgType("USER");             //指定用户
                sysAnnouncement.setMsgCategory("2");
                sysAnnouncementService.saveAnnouncement(sysAnnouncement);

                SysUser receiver = sysUserMapper.selectById(id);
                RTXConfig.sendNotify(receiver.getActivitiSync(), "会议申请", "您有一条会议申请待审批");

                //移动端提醒
                WebSocketServer.SendMessage("您有一条会议申请待审批", id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
