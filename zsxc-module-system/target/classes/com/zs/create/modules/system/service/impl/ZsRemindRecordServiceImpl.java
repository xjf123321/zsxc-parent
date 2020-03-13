package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.entity.ZsRemindRecord;
import com.zs.create.modules.system.entity.ZsSqNotice;
import com.zs.create.modules.system.mapper.ZsRemindRecordMapper;
import com.zs.create.modules.system.service.ZsRemindRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息提醒 service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsRemindRecordServiceImpl extends ServiceImpl<ZsRemindRecordMapper, ZsRemindRecord> implements ZsRemindRecordService {

    @Autowired
    private ZsRemindRecordMapper zsRemindRecordMapper;

    @Override
    @Transactional
    public void add(ZsDocRecord zsDocRecord) {
        ZsRemindRecord zsRemindRecord = new ZsRemindRecord();
        zsRemindRecord.setTxtitle("您有公文审批单需要处理");
        zsRemindRecord.setContent("请审批公文:" + zsDocRecord.getTitle());
        zsRemindRecord.setTxdx(zsDocRecord.getApprovalPerson());
        zsRemindRecord.setTxsj(new Date());
        zsRemindRecord.setTxlx("3");
        zsRemindRecord.setOperator(zsDocRecord.getUserId());
        zsRemindRecord.setBz(zsDocRecord.getRemarks());
        zsRemindRecordMapper.insert(zsRemindRecord);
    }

    @Override
    @Transactional
    public void addNotice(ZsSqNotice zsSqNotice) {
        ZsRemindRecord zsRemindRecord = new ZsRemindRecord();
        zsRemindRecord.setTxtitle("您有公告审批单需要处理");
        zsRemindRecord.setContent("请审批公告:" + zsSqNotice.getTitle());
        zsRemindRecord.setTxdx(zsSqNotice.getApprovalPerson());
        zsRemindRecord.setTxsj(new Date());
        zsRemindRecord.setTxlx("3");
        zsRemindRecord.setOperator(zsSqNotice.getUserId());
        zsRemindRecord.setBz(zsSqNotice.getRemarks());
        zsRemindRecordMapper.insert(zsRemindRecord);
    }

    @Override
    public List<ZsRemindRecord> select(Date txsj) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        List<ZsRemindRecord> zsRemindRecordList = new ArrayList<>();
        zsRemindRecordList = zsRemindRecordMapper.select(txsj, sysUser.getId());
        return zsRemindRecordList;
    }
}
