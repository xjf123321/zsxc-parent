package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.modules.system.entity.DocRecord;
import com.zs.create.modules.system.entity.SysAnnouncement;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.mapper.DocRecordMapper;
import com.zs.create.modules.system.mapper.SysUserMapper;
import com.zs.create.modules.system.mapper.ZsDocRecordMapper;
import com.zs.create.modules.system.service.DocRecordService;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import com.zs.create.modules.system.service.ZsRemindRecordService;
import com.zs.create.modules.system.vo.ZsDocRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Set;

@Slf4j
@Service
public class DocRecordServiceImpl extends ServiceImpl<DocRecordMapper, DocRecord> implements DocRecordService {

    @Autowired
    private DocRecordMapper docRecordMapper;

    @Autowired
    private ZsDocRecordMapper zsDocRecordMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    @Transactional
    public void add(ZsDocRecord zsDocRecord) {
        zsDocRecordMapper.updateStatus(zsDocRecord.getId(), "0");
        String approvalPerson = zsDocRecord.getApprovalPerson();
        if (approvalPerson.contains(",")) {
            String[] arr = approvalPerson.split(",");
            for (int i = 0; i < arr.length; i++) {
                DocRecord docRecord = new DocRecord();
                docRecord.setApplyer(zsDocRecord.getUserId());
                docRecord.setCreateTime(new Date());
                docRecord.setDocId(zsDocRecord.getId());
                docRecord.setApprovalPerson(arr[i]);
                docRecord.setApprovalStatus("0");
                docRecord.setApprovalOpinion(zsDocRecord.getApprovalOpinion());
                docRecord.setUserName(zsDocRecord.getUsername());
                docRecord.setEmergencyLevel(zsDocRecord.getEmergencyLevel());
                docRecord.setPlayName("公文申请");
                docRecordMapper.insert(docRecord);
            }
        } else {
            DocRecord docRecord = new DocRecord();
            docRecord.setApplyer(zsDocRecord.getUserId());
            docRecord.setCreateTime(new Date());
            docRecord.setDocId(zsDocRecord.getId());
            docRecord.setApprovalPerson(zsDocRecord.getApprovalPerson());
            docRecord.setApprovalStatus("0");
            docRecord.setApprovalOpinion(zsDocRecord.getApprovalOpinion());
            docRecord.setUserName(zsDocRecord.getUsername());
            docRecord.setEmergencyLevel(zsDocRecord.getEmergencyLevel());
            docRecord.setPlayName("公文申请");
            docRecordMapper.insert(docRecord);
        }
    }

    @Override
    @Transactional
    public void docRecordAdd(ZsDocRecord zsDocRecord) {
        DocRecord docRecord = new DocRecord();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        docRecord.setDocId(zsDocRecord.getId());
        docRecord.setPlayName("审批同意");      //操作名称
        docRecordMapper.updateDocRecordStatus(sysUser.getId(), docRecord.getDocId(), docRecord.getPlayName(), zsDocRecord.getApprovalOpinion(), sysUser.getRealname(), zsDocRecord.getRemarks());

        //对其他领导审批的审批状态进行判断
        String docId = docRecord.getDocId();
        Set<String> set = docRecordMapper.selectStatus(docId);
        if (set.size() == 1) {
            if (set.contains("2")) {
                zsDocRecordMapper.updateStatus(docId, "2");
                //docRecordMapper.updateStatus(sysUser.getId(),docId,"2");
            } else if (set.contains("1")) {
                zsDocRecordMapper.updateStatus(docId, "1");
                //docRecordMapper.updateStatus(sysUser.getId(),docId,"1");
            }
        } else {
            if (set.contains("2")) {
                zsDocRecordMapper.updateStatus(docId, "2");
                //docRecordMapper.updateStatus(sysUser.getId(),docId,"2");
            }
        }
    }

    @Override
    @Transactional
    public void notDocRecordAdd(ZsDocRecord zsDocRecord) {
        DocRecord docRecord = new DocRecord();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        docRecord.setDocId(zsDocRecord.getId());
        docRecord.setPlayName("审批不同意");
        String docId = docRecord.getDocId();
        docRecordMapper.updateNotDocRecordStatus(sysUser.getId(), docRecord.getDocId(), docRecord.getPlayName(), zsDocRecord.getApprovalOpinion(), sysUser.getRealname());
        zsDocRecordMapper.updateStatus(docId, "2");
    }

    @Override
    @Transactional
    public void addBatch(ZsDocRecord zsDocRecord) {
        zsDocRecordMapper.updateStatus(zsDocRecord.getId(), "0");
        ZsDocRecord zsDocRecord1 = zsDocRecordMapper.formShow(zsDocRecord.getId());
        zsDocRecord1.setApprovalPerson(zsDocRecord.getApprovalPerson());
        String approvalPerson = zsDocRecord1.getApprovalPerson();
        String[] arr = approvalPerson.split(",");
        for (int i = 0; i < arr.length; i++) {
            DocRecord docRecord = new DocRecord();
            docRecord.setApplyer(zsDocRecord1.getUserId());
            docRecord.setCreateTime(new Date());
            docRecord.setDocId(zsDocRecord1.getId());
            docRecord.setApprovalPerson(arr[i]);
            docRecord.setApprovalStatus("0");
            docRecord.setApprovalOpinion(zsDocRecord1.getApprovalOpinion());
            docRecord.setUserName(zsDocRecord1.getUsername());
            docRecord.setEmergencyLevel(zsDocRecord1.getEmergencyLevel());
            docRecord.setPlayName("通知公告申请");
            docRecordMapper.insert(docRecord);
        }
        //消息提醒
        for (int i = 0; i < arr.length; i++) {
            //pc端消息提醒
            SysAnnouncement sysAnnouncement = new SysAnnouncement();
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
            sysAnnouncement.setUserIds(arr[i]+",");    //指定用户
            sysAnnouncement.setTitile("您有一条公文申请待审批");
            sysAnnouncement.setSendTime(new Date());        //发布时间
            sysAnnouncement.setPriority(zsDocRecord1.getEmergencyLevel());     //紧急程度
            sysAnnouncement.setMsgType("USER");             //指定用户
            sysAnnouncement.setMsgCategory("2");
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);

            SysUser receiver = sysUserMapper.selectById(arr[i]);
            RTXConfig.sendNotify(receiver.getActivitiSync(), "公文申请", "您有一条公文申请待审批");
            //pad端消息提醒
            try {
                WebSocketServer.SendMessage("您有一条公文需要审批", arr[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void addProcess(ZsDocRecord zsDocRecord) {
        ZsDocRecord zsDocRecord1 = zsDocRecordMapper.select(zsDocRecord.getId());
        DocRecord docRecord = new DocRecord();
        docRecord.setApplyer(zsDocRecord1.getUserId());
        docRecord.setCreateTime(new Date());
        docRecord.setDocId(zsDocRecord1.getId());
        docRecord.setApprovalPerson(zsDocRecord.getApprovalPerson());
        docRecord.setApprovalStatus("0");
        String username = sysUserMapper.selectById(zsDocRecord.getApprovalPerson()).getUsername();
        docRecord.setUserName(username);
        docRecord.setEmergencyLevel(zsDocRecord1.getEmergencyLevel());
        docRecordMapper.insert(docRecord);
    }
}
