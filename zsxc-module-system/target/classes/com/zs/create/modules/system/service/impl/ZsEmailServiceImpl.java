package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.QueryEmail;
import com.zs.create.modules.system.entity.ZsEmail;
import com.zs.create.modules.system.entity.ZsEmailContent;
import com.zs.create.modules.system.entity.ZsEmailRecording;
import com.zs.create.modules.system.mapper.ZsEmailContentMapper;
import com.zs.create.modules.system.mapper.ZsEmailMapper;
import com.zs.create.modules.system.mapper.ZsEmailRecordingMapper;
import com.zs.create.modules.system.service.ZsEmailService;
import com.zs.create.modules.system.vo.ZsEmailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 邮件表 service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsEmailServiceImpl extends ServiceImpl<ZsEmailMapper, ZsEmail> implements ZsEmailService {

    @Autowired
    private ZsEmailMapper zsEmailMapper;

    @Autowired
    private ZsEmailContentMapper zsEmailContentMapper;

    @Autowired
    private ZsEmailRecordingMapper zsEmailRecordingMapper;

    @Override
    @Transactional
    public void addEmail(ZsEmailVO zsEmailVO) {
        /*if (zsEmailVO.getId() != null &&  !"".equals(zsEmailVO.getId())) {
            zsEmailMapper.updateDel(zsEmailVO.getId());
        }*/

        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsEmail zsEmail = new ZsEmail();
        zsEmail.setSend(sysUser.getId());
        zsEmail.setSendName(sysUser.getRealname());
        zsEmail.setReceiver(zsEmailVO.getReceiver());
        zsEmail.setTitle(zsEmailVO.getTitle());
        zsEmail.setStatus(1);
        zsEmail.setCreateTime(new Date());
        if (zsEmailVO.getMsgContent() !=  null && !"".equals(zsEmailVO.getMsgContent()) && zsEmailVO.getMsgContent().length() >= 50) {
            zsEmail.setDescription(zsEmailVO.getMsgContent().substring(0, 50));
        } else {
            zsEmail.setDescription(zsEmailVO.getMsgContent());
        }
        zsEmail.setType(0);
        zsEmail.setDelFlag(0);
        zsEmail.setSendStatus(1);
        zsEmail.setIds(zsEmailVO.getIds());
        zsEmailMapper.insert(zsEmail);

        String receiver = zsEmail.getIds();
        String[] receivers = receiver.trim().split(",");
        //遍历收件人 存储发件记录
        for (String s : receivers) {
            ZsEmailRecording recording = getRecording(0, zsEmail, s);
            zsEmailRecordingMapper.insert(recording);
        }

        ZsEmailContent content = new ZsEmailContent();
        content.setMsgContent(zsEmailVO.getMsgContent());
        content.setAnnex(zsEmailVO.getAnnex());
        content.setEmailId(zsEmail.getId());
        zsEmailContentMapper.insert(content);
    }

    @Override
    @Transactional
    public void addDraft(ZsEmailVO zsEmailVO) {
        if (zsEmailVO.getId() != null && !"".equals(zsEmailVO.getId())) {
            ZsEmail zsEmail = new ZsEmail();
            zsEmail.setId(zsEmailVO.getId());
            zsEmail.setDelFlag(2);
            zsEmailMapper.updateById(zsEmail);
        }
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsEmail zsEmail = new ZsEmail();
        zsEmail.setSend(sysUser.getId());
        zsEmail.setSendName(sysUser.getRealname());
        zsEmail.setReceiver(zsEmailVO.getReceiver());
        zsEmail.setTitle(zsEmailVO.getTitle());
        zsEmail.setStatus(0);
        zsEmail.setCreateTime(new Date());
        if (zsEmailVO.getMsgContent() !=  null && !"".equals(zsEmailVO.getMsgContent()) && zsEmailVO.getMsgContent().length() >= 50) {
            zsEmail.setDescription(zsEmailVO.getMsgContent().substring(0, 50));
        } else {
            zsEmail.setDescription(zsEmailVO.getMsgContent());
        }
        zsEmail.setType(0);
        zsEmail.setDelFlag(0);
        zsEmail.setSendStatus(1);
        zsEmail.setIds(zsEmailVO.getIds());
        zsEmailMapper.insert(zsEmail);

        ZsEmailContent content = new ZsEmailContent();
        content.setMsgContent(zsEmailVO.getMsgContent());
        content.setAnnex(zsEmailVO.getAnnex());
        content.setEmailId(zsEmail.getId());
        zsEmailContentMapper.insert(content);
    }

    @Override
    @Transactional
    public void updateStatus(ZsEmail zsEmail) {
        zsEmailMapper.updateById(zsEmail);
    }

    @Override
    public ZsEmailVO reply(String id) {
        ZsEmailVO zsEmailVO = zsEmailMapper.findById(id);
        ZsEmailVO emailVO = new ZsEmailVO();
        emailVO.setIds(zsEmailVO.getSend());
        emailVO.setSendName(zsEmailVO.getSendName());
        emailVO.setTitle(zsEmailVO.getTitle());
        emailVO.setMsgContent(zsEmailVO.getMsgContent());
        emailVO.setAnnex(zsEmailVO.getAnnex());
        return emailVO;
    }

    @Override
    public ZsEmailVO forward(String id) {
        ZsEmailVO record = zsEmailMapper.findById(id);
        ZsEmailVO zsEmailVO = new ZsEmailVO();

        zsEmailVO.setTitle(record.getTitle());
        zsEmailVO.setIds(record.getIds());
        zsEmailVO.setAnnex(record.getAnnex());
        zsEmailVO.setMsgContent(record.getMsgContent());
        if (record.getAnnex() != null && !"".equals(record.getAnnex())) {
            String[] split = record.getAnnex().trim().split(",");
            List<String > files = new ArrayList();
            for (String file : split) {
                files.add(file);
            }
            zsEmailVO.setFiles(files);
        }

        return zsEmailVO;
    }

    @Override
    public Page<ZsEmailVO> queryPage(Page<ZsEmailVO> page, QueryWrapper queryWrapper) {
        return zsEmailMapper.queryPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public void updateDel(String id) {
        zsEmailMapper.updateDel(id);
    }

    @Override
    @Transactional
    public void updateByIds(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        for (String s : list) {
            zsEmailMapper.updateDel(s);
        }
    }

    @Override
    public ZsEmailVO findById(String id) {
        ZsEmailVO emailVO = zsEmailMapper.findById(id);
        if (emailVO.getAnnex() != null &&  !"".equals(emailVO.getAnnex())) {
            String[] split = emailVO.getAnnex().trim().split(",");
            List<String > files = new ArrayList();
            for (String file: split) {
                files.add(file);
            }
            emailVO.setFiles(files);
        }
        return emailVO;
    }

    @Override
    public Page<ZsEmailVO> searchInbox(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword) {
        QueryEmail queryEmail = new QueryEmail();
        queryEmail.setSendName(keyword);
        queryEmail.setTitle(keyword);
        queryEmail.setMsgContent(keyword);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Page<ZsEmailVO> emailVOPage = zsEmailMapper.searchInbox(page, zsEmailVO, sysUser.getId(), queryEmail);
        return getPage(emailVOPage);
    }

    @Override
    public Page<ZsEmailVO> searchOutbox(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword) {
        QueryEmail queryEmail = new QueryEmail();
        queryEmail.setReceiver(keyword);
        queryEmail.setTitle(keyword);
        queryEmail.setMsgContent(keyword);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Page<ZsEmailVO> emailVOPage = zsEmailMapper.searchOutbox(page, zsEmailVO, sysUser.getId(), queryEmail);
        return getPage(emailVOPage);
    }

    @Override
    public Page<ZsEmailVO> searchDrafts(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword) {
        QueryEmail queryEmail = new QueryEmail();
        queryEmail.setReceiver(keyword);
        queryEmail.setTitle(keyword);
        queryEmail.setMsgContent(keyword);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Page<ZsEmailVO> emailVOPage = zsEmailMapper.searchDrafts(page, zsEmailVO, sysUser.getId(), queryEmail);
        return getPage(emailVOPage);
    }

    @Override
    public Page<ZsEmailVO> searchDustbin(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword) {
        QueryEmail queryEmail = new QueryEmail();
        queryEmail.setSendName(keyword);
        queryEmail.setTitle(keyword);
        queryEmail.setMsgContent(keyword);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Page<ZsEmailVO> emailVOPage = zsEmailMapper.searchDustbin(page, zsEmailVO, sysUser.getId(), queryEmail);
        return getPage(emailVOPage);
    }




    /**
     *
     *根据发送的类型增加记录表数据
     */
    public ZsEmailRecording getRecording(Integer state, ZsEmail zsEmail, String loginName) {
        ZsEmailRecording recording = new ZsEmailRecording();
        recording.setState(state);
        recording.setReadType(0);
        recording.setDelFlag(0);
        recording.setEmailId(zsEmail.getId());
        recording.setLoginName(loginName);
        return recording;
    }

    /*
    * 将多个邮件附件赋值到files属性
    * */
    public Page<ZsEmailVO> getPage(Page<ZsEmailVO> emailVOPage) {
        for (ZsEmailVO emailVO : emailVOPage.getRecords()) {
            if (emailVO.getAnnex() != null &&  !"".equals(emailVO.getAnnex())) {
                String[] split = emailVO.getAnnex().trim().split(",");
                List<String > files = new ArrayList();
                for (String file: split) {
                    files.add(file);
                }
                emailVO.setFiles(files);
            }
        }
        return emailVOPage;
    }


}
