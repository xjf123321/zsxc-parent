package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;

import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.modules.process.entity.ZsBackGuocheng;
import com.zs.create.modules.process.mapper.ZsBackGuochengMapper;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import com.zs.create.modules.system.service.ZsSqBackService;

import com.zs.create.modules.workflow.instance.service.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 销假
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-18
 * */
@Service
@Slf4j
public class ZsSqBackServiceImpl extends ServiceImpl<ZsSqBackMapper, ZsSqBack> implements ZsSqBackService {

    @Autowired
    private ZsSqBackMapper zsSqBackMapper;

    @Autowired
    private IProcessInstanceService processInstanceService;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private ZsBackGuochengMapper zsBackGuochengMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysUserMapper sysUserMapper;



    @Override
    @Transactional
    public void add(ZsSqBack zsSqBack) throws Exception {
        try {
            zsSqBack.setDelFlag("0");
            zsSqBack.setStatus("0");
            zsSqBack.setCreateTime(new Date());
            zsSqBack.setApplyType("3");
            Subject subject = SecurityUtils.getSubject();
            LoginUser sysUser = (LoginUser) subject.getPrincipal();
            String userId = sysUser.getId();
            String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);   //当前登陆人部门id
            zsSqBack.setUsername(sysUser.getRealname());
            zsSqBack.setUserId(sysUser.getId());
            zsSqBack.setApplyerDept(deptId);
            zsSqBackMapper.insert(zsSqBack);
            //插入过程表中
            ZsBackGuocheng zsBackGuocheng = new ZsBackGuocheng();
            zsBackGuocheng.setBackId(zsSqBack.getId());
            zsBackGuocheng.setCreateTime(new Date());
            zsBackGuocheng.setState("0");
            //zsBackGuocheng.setUserId(sysUser.getId());
            zsBackGuocheng.setUserName(sysUser.getRealname());
            zsBackGuocheng.setPlayName("销假申请提交");
            zsBackGuochengMapper.insert(zsBackGuocheng);

            //pc端提醒
            List<String> usersId = sysUserDepartMapper.selectIdsByDeptId(deptId);
            String ids = "";
            for (String id : usersId) {
                ids += id + ",";
            }
            SysAnnouncement sysAnnouncement = new SysAnnouncement();
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
            sysAnnouncement.setUserIds(ids);    //指定用户
            sysAnnouncement.setTitile("您有一条销假申请待审批");
            sysAnnouncement.setSendTime(new Date());        //发布时间
            sysAnnouncement.setPriority(zsSqBack.getEmergencyLevel());     //紧急程度
            sysAnnouncement.setMsgType("USER");             //指定用户
            sysAnnouncement.setMsgCategory("2");
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);

            for (String id : usersId) {
                try {
                    SysUser receiver = sysUserMapper.selectById(id);
                    RTXConfig.sendNotify(receiver.getActivitiSync(), "销假申请", "您有一条销假申请待审批");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //移动端提醒
            for (String id: usersId) {
                try  {
                    WebSocketServer.SendMessage("您有一条销假申请待审批", id);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new Exception("SavezsSqBackError");
        }
    }

    @Override
    public ZsSqBack selectById(String id) throws Exception {
        ZsSqBack zsSqBack = zsSqBackMapper.selectById(id);
        return zsSqBack;
    }

    @Override
    public IPage<ZsSqBack> backApplyList(String emergencyLevel, Page<ZsSqBack> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        for (SysRole sysRole: roleList) {
            if (sysRole.getRoleCode().equals("BMJL")) {   //判断当前用户是否包含BMJL角色
                String state = "0";     //领导待审批
                Long count = zsSqBackMapper.backApplyCount(deptId, state, emergencyLevel, username);
                List<ZsSqBack> bpmnWorkIPage = zsSqBackMapper.backApplyList(page, deptId, state, emergencyLevel, username);
                page.setRecords(bpmnWorkIPage);
                page.setTotal(count);
            }
        }
        return page;
    }

    @Override
    public Map formShow(String id) {
        Map map = new HashMap();
        ZsSqBack zsSqBack = zsSqBackMapper.selectById(id);
        List<ZsBackGuocheng> records = zsBackGuochengMapper.formShow(id);
        map.put("formData", zsSqBack);
        map.put("processList", records);
        return map;
    }

    @Override
    public IPage<ZsSqBack> personnelList(Page<ZsSqBack> page) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = zsSqBackMapper.personnelCount();
        List<ZsSqBack> bpmnWorkIPage = zsSqBackMapper.personnelList(page);
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    public IPage<ZsSqBack> allowList(String realname, Page<ZsSqBack> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId()); //当前登陆人部门id
        Long count = zsSqBackMapper.allowListCount(sysUser.getId(), username);
        List<ZsSqBack> zsSqRoomList = zsSqBackMapper.allowList(page, sysUser.getId(), username);
        page.setRecords(zsSqRoomList);
        page.setTotal(count);
        return page;
    }

    @Override
    @Transactional
    public void updateStatus(ZsSqBack zsSqBack) {
        zsSqBackMapper.updateStatus(zsSqBack.getId(),zsSqBack.getStatus());
    }
}
