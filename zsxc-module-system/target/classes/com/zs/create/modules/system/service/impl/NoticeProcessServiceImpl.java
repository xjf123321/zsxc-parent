package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.modules.system.entity.NoticeProcess;
import com.zs.create.modules.system.entity.SysAnnouncement;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.ZsSqNotice;
import com.zs.create.modules.system.mapper.NoticeProcessMapper;
import com.zs.create.modules.system.mapper.SysUserMapper;
import com.zs.create.modules.system.mapper.ZsSqNoticeMapper;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import com.zs.create.modules.system.service.NoticeProcessService;
import com.zs.create.modules.system.service.ZsRemindRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class NoticeProcessServiceImpl extends ServiceImpl<NoticeProcessMapper, NoticeProcess> implements NoticeProcessService {
    @Autowired
    private NoticeProcessMapper noticeProcessMapper;

    @Autowired
    private ZsSqNoticeMapper zsSqNoticeMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional
    public void add(ZsSqNotice zsSqNotice) {
        zsSqNoticeMapper.updateStatus(zsSqNotice.getId(), "0");
        String approvalPerson = zsSqNotice.getApprovalPerson();
        String[] arr = approvalPerson.split(",");
        for (int i = 0; i < arr.length; i++) {
            NoticeProcess noticeProcess = new NoticeProcess();
            noticeProcess.setApplyer(zsSqNotice.getUserId());
            noticeProcess.setCreateTime(new Date());
            noticeProcess.setDocId(zsSqNotice.getId());
            noticeProcess.setApprovalPerson(arr[i]);
            noticeProcess.setApprovalStatus("0");
            noticeProcess.setApprovalOpinion(zsSqNotice.getApprovalOpinion());
            noticeProcess.setUserName(zsSqNotice.getUsername());
            noticeProcess.setEmergencyLevel(zsSqNotice.getEmergencyLevel());
            noticeProcess.setPlayName("通知公告申请");
            noticeProcessMapper.insert(noticeProcess);
        }
    }

    @Override
    @Transactional
    public void noticeProcessdAdd(ZsSqNotice zsSqNotice) {
        //zsSqNoticeMapper.updateStatus(zsSqNotice.getId(),"0");
        NoticeProcess noticeProcess = new NoticeProcess();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        noticeProcess.setDocId(zsSqNotice.getId());
        noticeProcess.setPlayName("审批同意");
        noticeProcessMapper.updateNoticeProcessStatus(sysUser.getId(), noticeProcess.getDocId(), noticeProcess.getPlayName(),
                zsSqNotice.getApprovalOpinion(), sysUser.getRealname(), zsSqNotice.getRemarks());

        //对其他领导审批的审批状态进行判断
        String docId = noticeProcess.getDocId();
        Set<String> set = noticeProcessMapper.selectStatus(docId);
        if (set.size() == 1) {
            if (set.contains("2")) {
                zsSqNoticeMapper.updateStatus(docId, "2");
                //noticeProcessMapper.updateStatus(sysUser.getId(),docId,"2");
            } else if (set.contains("1")) {
                zsSqNoticeMapper.updateStatus(docId, "1");
                //noticeProcessMapper.updateStatus(sysUser.getId(),docId,"1");
            }
        } else {
            if (set.contains("2")) {
                zsSqNoticeMapper.updateStatus(docId, "2");
                //noticeProcessMapper.updateStatus(sysUser.getId(),docId,"2");
            }
        }
    }

    @Override
    @Transactional
    public void notNoticeProcessdAdd(ZsSqNotice zsSqNotice) {
        NoticeProcess noticeProcess = new NoticeProcess();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        noticeProcess.setDocId(zsSqNotice.getId());
        noticeProcess.setPlayName("审批不同意");
        String docId = noticeProcess.getDocId();
        noticeProcessMapper.updateNotNoticeProcessStatus(sysUser.getId(), noticeProcess.getDocId(), noticeProcess.getPlayName(), zsSqNotice.getApprovalOpinion(), sysUser.getRealname());
        zsSqNoticeMapper.updateStatus(docId, "2");
    }

    @Override
    @Transactional
    public void addBatch(ZsSqNotice zsSqNotice) {
        zsSqNoticeMapper.updateStatus(zsSqNotice.getId(), "0");
        ZsSqNotice zsSqNotice1 = zsSqNoticeMapper.formShow(zsSqNotice.getId());
        zsSqNotice1.setApprovalPerson(zsSqNotice.getApprovalPerson());
        String approvalPerson = zsSqNotice1.getApprovalPerson();
        String[] arr = approvalPerson.split(",");
        for (int i = 0; i < arr.length; i++) {
            NoticeProcess noticeProcess = new NoticeProcess();
            noticeProcess.setApplyer(zsSqNotice1.getUserId());
            noticeProcess.setCreateTime(new Date());
            noticeProcess.setDocId(zsSqNotice1.getId());
            noticeProcess.setApprovalPerson(arr[i]);
            noticeProcess.setApprovalStatus("0");
            noticeProcess.setApprovalOpinion(zsSqNotice1.getApprovalOpinion());
            noticeProcess.setUserName(zsSqNotice1.getUsername());
            noticeProcess.setEmergencyLevel(zsSqNotice1.getEmergencyLevel());
            noticeProcess.setPlayName("通知公告申请");
            noticeProcessMapper.insert(noticeProcess);
        }
        //提醒
        for (int i = 0; i < arr.length; i++) {
            //pc端消息提醒
            SysAnnouncement sysAnnouncement = new SysAnnouncement();
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
            sysAnnouncement.setUserIds(arr[i]+",");    //指定用户
            sysAnnouncement.setTitile("您有一条公告申请待审批");
            sysAnnouncement.setSendTime(new Date());        //发布时间
            sysAnnouncement.setPriority(zsSqNotice1.getEmergencyLevel());     //紧急程度
            sysAnnouncement.setMsgType("USER");             //指定用户
            sysAnnouncement.setMsgCategory("2");
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);

        }
    }

    @Override
    @Transactional
    public void addProcess(ZsSqNotice zsSqNotice) {
        ZsSqNotice zsSqNotice1 = zsSqNoticeMapper.select(zsSqNotice.getId());
        NoticeProcess noticeProcess = new NoticeProcess();
        noticeProcess.setApplyer(zsSqNotice1.getUserId());
        noticeProcess.setCreateTime(new Date());
        noticeProcess.setDocId(zsSqNotice1.getId());
        noticeProcess.setApprovalPerson(zsSqNotice.getApprovalPerson());
        noticeProcess.setApprovalStatus("0");
        String username = sysUserMapper.selectById(zsSqNotice.getApprovalPerson()).getUsername();
        noticeProcess.setUserName(username);
        noticeProcess.setEmergencyLevel(zsSqNotice1.getEmergencyLevel());
        noticeProcessMapper.insert(noticeProcess);
    }
}
