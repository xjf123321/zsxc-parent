package com.zs.create.modules.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import com.zs.create.modules.process.mapper.ZsLeaveGuochengMapper;
import com.zs.create.modules.process.service.ZsSqLeaveTwoService;
import com.zs.create.modules.system.entity.SysAnnouncement;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.mapper.SysDepartMapper;
import com.zs.create.modules.system.mapper.SysUserDepartMapper;
import com.zs.create.modules.system.mapper.SysUserMapper;
import com.zs.create.modules.system.mapper.ZsSqLeaveMapper;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @Author yaochao
 * @since 2019-12-19
 */
@Service
@Slf4j
public class ZsSqLeaveTwoServiceImpl extends ServiceImpl<ZsSqLeaveMapper, ZsSqLeave> implements ZsSqLeaveTwoService {

    @Autowired
    private ZsSqLeaveMapper zsSqLeaveMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private ZsLeaveGuochengMapper zsLeaveGuochengMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Override
    public IPage<ZsSqLeave> findList(Page<ZsSqLeave> page, ZsSqLeave zsSqLeave) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        IPage<ZsSqLeave> pageList = zsSqLeaveMapper.findList(page, zsSqLeave, sysUser.getId());
        List<ZsSqLeave> receiver = getReceiver(pageList.getRecords());
        pageList.setRecords(receiver);
        return pageList;
    }

    @Override
    @Transactional
    public void add(ZsSqLeave zsSqLeave) throws Exception {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        try {
            //申请表插数据
            zsSqLeave.setStatus("0");
            zsSqLeave.setDelFlag("0");
            zsSqLeave.setCreateTime(new Date());
            zsSqLeave.setApplyType("2");
            zsSqLeave.setUsername(sysUser.getRealname());
            zsSqLeave.setUserId(sysUser.getId());
            zsSqLeave.setApplyerDept(deptId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int count = workDays(sdf.format(zsSqLeave.getStartDate()), sdf.format(zsSqLeave.getEndDate()));
            zsSqLeave.setNumber(String.valueOf(count));
            zsSqLeaveMapper.add(zsSqLeave);

            //过程表插数据
            ZsLeaveGuocheng guocheng = new ZsLeaveGuocheng();
            guocheng.setLeaveId(zsSqLeave.getId());
            guocheng.setPlayName("请假申请提交");
            guocheng.setUserName(sysUser.getRealname());
            guocheng.setState("3");
            zsLeaveGuochengMapper.insert(guocheng);
            for (Map<String, String> map : zsSqLeave.getReceiver()) {
                ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
                zsLeaveGuocheng.setLeaveId(zsSqLeave.getId());
                zsLeaveGuocheng.setState("0");
                zsLeaveGuocheng.setApprovalPerson(map.get("name"));
                zsLeaveGuocheng.setNumber(map.get("key"));
                zsLeaveGuocheng.setFlag("-2");
                if ("1".equals(map.get("key"))) {
                    zsLeaveGuocheng.setFlag("1");
                    //pc端提醒
                    SysAnnouncement sysAnnouncement = new SysAnnouncement();
                    sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                    sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                    sysAnnouncement.setUserIds(map.get("name") + ",");    //指定用户
                    sysAnnouncement.setTitile("您有一条请假申请待审批");
                    sysAnnouncement.setSendTime(new Date());        //发布时间
                    sysAnnouncement.setPriority(zsSqLeave.getEmergencyLevel());     //紧急程度
                    sysAnnouncement.setMsgType("USER");             //指定用户
                    sysAnnouncement.setMsgCategory("2");
                    sysAnnouncementService.saveAnnouncement(sysAnnouncement);
                    //移动端消息提醒
                    try {
                        SysUser receiver = sysUserMapper.selectById(map.get("name"));
                        RTXConfig.sendNotify(receiver.getActivitiSync(), "请假申请", "您有一条请假申请待审批");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                zsLeaveGuochengMapper.insert(zsLeaveGuocheng);
            }
        }  catch (Exception e) {
            throw new Exception("SaveZsSqLeave Error");
        }
        //提醒信息
        for (Map<String, String> map : zsSqLeave.getReceiver()) {
            if ("1".equals(map.get("key"))) {
                try {
                    WebSocketServer.SendMessage("您有一条请假申请待审批", map.get("name"));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public IPage<ZsSqLeave> leaveApplyList(Page<ZsSqLeave> page, String emergencyLevel, String title, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        IPage<ZsSqLeave> pageList = zsSqLeaveMapper.needDoList(page, sysUser.getId(), emergencyLevel, title, username);
        return pageList;
    }

    @Override
    @Transactional
    public void updateState(ZsSqLeave zsSqLeave) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
        zsLeaveGuocheng.setState("1");
        //zsLeaveGuocheng.setFlag("-1");
        zsLeaveGuocheng.setUserId(sysUser.getId());
        zsLeaveGuocheng.setUserName(sysUser.getRealname());
        zsLeaveGuocheng.setCreateTime(new Date());
        zsLeaveGuocheng.setApprovalOpinion(zsSqLeave.getApprovalOpinion());
        zsLeaveGuocheng.setPlayName("领导同意");

        QueryWrapper qw = new QueryWrapper();
        qw.eq("leave_id", zsSqLeave.getId());
        qw.eq("approval_person", sysUser.getId());
        qw.eq("state", "0");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("leave_id", zsSqLeave.getId());
        queryWrapper.eq("state", "0");

        ZsLeaveGuocheng one = zsLeaveGuochengMapper.selectOne(qw); //当前审核人记录
        String newNumber = String.valueOf(Integer.parseInt(one.getFlag()) + 1);
        zsLeaveGuochengMapper.update(zsLeaveGuocheng, qw); //修改当前审核人记录表数据
        List<ZsLeaveGuocheng> list = zsLeaveGuochengMapper.selectList(queryWrapper); //当前申请记录所有审核人
        if (list.size() == 0) {
            //流程结束
            ZsSqLeave sqLeave = new ZsSqLeave();
            sqLeave.setId(one.getLeaveId());
            sqLeave.setStatus("1");
            zsSqLeaveMapper.updateById(sqLeave);
        } else {
            int count = 0;
            for (ZsLeaveGuocheng guocheng : list) {
                if (Integer.parseInt(newNumber) > Integer.parseInt(guocheng.getNumber())) {
                    count++;
                }
                if (newNumber.equals(guocheng.getNumber())) {
                    guocheng.setFlag(guocheng.getNumber());
                    zsLeaveGuochengMapper.updateById(guocheng);
                    //pc端消息提醒
                    ZsSqLeave sqLeave = zsSqLeaveMapper.findById(zsSqLeave.getId());
                    SysAnnouncement sysAnnouncement = new SysAnnouncement();
                    sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                    sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                    sysAnnouncement.setUserIds(guocheng.getApprovalPerson() + ",");    //指定用户
                    sysAnnouncement.setTitile("您有一条请假申请待审批");
                    sysAnnouncement.setSendTime(new Date());        //发布时间
                    sysAnnouncement.setPriority(sqLeave.getEmergencyLevel());     //紧急程度
                    sysAnnouncement.setMsgType("USER");             //指定用户
                    sysAnnouncement.setMsgCategory("2");
                    sysAnnouncementService.saveAnnouncement(sysAnnouncement);
                    //移动端消息提醒
                    try {
                        SysUser receiver = sysUserMapper.selectById(guocheng.getApprovalPerson());
                        RTXConfig.sendNotify(receiver.getActivitiSync(), "请假申请", "您有一条请假申请待审批");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        WebSocketServer.SendMessage("您有一条请假申请待审批", guocheng.getApprovalPerson());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
                if (count == list.size()) {
                    //流程结束
                    ZsSqLeave sqLeave = new ZsSqLeave();
                    sqLeave.setId(guocheng.getLeaveId());
                    sqLeave.setStatus("1");
                    zsSqLeaveMapper.updateById(sqLeave);
                }
            }
        }


    }

    @Override
    @Transactional
    public void disagree(ZsSqLeave zsSqLeave) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ZsSqLeave sqLeave = new ZsSqLeave();
        sqLeave.setId(zsSqLeave.getId());
        sqLeave.setStatus("2"); //驳回状态
        zsSqLeaveMapper.updateById(sqLeave);

        //当前申请单的过程记录
        QueryWrapper<ZsLeaveGuocheng> qw = new QueryWrapper<>();
        qw.eq("leave_id", zsSqLeave.getId());
        qw.eq("approval_person", sysUser.getId());
        qw.eq("state", "0");
        ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
        zsLeaveGuocheng.setState("2");
        //zsLeaveGuocheng.setFlag("-1");
        zsLeaveGuocheng.setUserId(sysUser.getId());
        zsLeaveGuocheng.setUserName(sysUser.getRealname());
        zsLeaveGuocheng.setCreateTime(new Date());
        zsLeaveGuocheng.setApprovalOpinion(zsSqLeave.getApprovalOpinion());
        zsLeaveGuocheng.setPlayName("领导不同意");
        zsLeaveGuochengMapper.update(zsLeaveGuocheng, qw);

        //删除未审批的过程记录
        /*QueryWrapper<ZsLeaveGuocheng> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("leave_id", zsSqLeave.getId());
        queryWrapper.eq("state", "0");
        zsLeaveGuochengMapper.delete(queryWrapper);*/
    }

    @Override
    public ZsSqLeave findById(String id) {
        return zsSqLeaveMapper.findById(id);
    }

    @Override
    public Map formShow(String id) {
        Map map = new HashMap();
        ZsSqLeave zsSqLeave = zsSqLeaveMapper.formShow(id);
        List<ZsLeaveGuocheng> zsLeaveGuochengList = zsLeaveGuochengMapper.formShow(id);
        map.put("formData", zsSqLeave);
        map.put("processList", zsLeaveGuochengList);
        return map;
    }

    @Override
    public IPage<ZsSqLeave> haveDoneList(Page<ZsSqLeave> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        IPage<ZsSqLeave> pageList = zsSqLeaveMapper.doneList(page, sysUser.getId(), username);
        return pageList;
    }


    @Override
    public IPage<ZsSqLeave> leaveList(Page<ZsSqLeave> page, ZsSqLeave zsSqLeave) {
        IPage<ZsSqLeave> pageList = zsSqLeaveMapper.leaveList(page, zsSqLeave);
        return pageList;
    }

    @Override
    @Transactional
    public void edit(ZsSqLeave zsSqLeave) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        try {

            //删除未审批的过程记录
            QueryWrapper<ZsLeaveGuocheng> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("leave_id", zsSqLeave.getId());
            queryWrapper.eq("state", "0");
            zsLeaveGuochengMapper.delete(queryWrapper);
            zsLeaveGuochengMapper.updateFlag(zsSqLeave.getId());
            //修改申请表记录
            zsSqLeave.setStatus("0");
            zsSqLeave.setDelFlag("0");
            zsSqLeave.setCreateTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int count = workDays(sdf.format(zsSqLeave.getStartDate()), sdf.format(zsSqLeave.getEndDate()));
            zsSqLeave.setNumber(String.valueOf(count));
            zsSqLeaveMapper.updateZsLeave(zsSqLeave);

            //过程表插数据
            ZsLeaveGuocheng guocheng = new ZsLeaveGuocheng();
            guocheng.setLeaveId(zsSqLeave.getId());
            guocheng.setPlayName("请假申请提交");
            guocheng.setUserName(sysUser.getRealname());
            guocheng.setState("3");
            zsLeaveGuochengMapper.insert(guocheng);
            for (Map<String, String> map : zsSqLeave.getReceiver()) {
                ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
                zsLeaveGuocheng.setLeaveId(zsSqLeave.getId());
                zsLeaveGuocheng.setState("0");
                zsLeaveGuocheng.setApprovalPerson(map.get("name"));
                zsLeaveGuocheng.setNumber(map.get("key"));
                zsLeaveGuocheng.setFlag("-2");
                if ("1".equals(map.get("key"))) {
                    zsLeaveGuocheng.setFlag("1");
                    //pc端提醒
                    SysAnnouncement sysAnnouncement = new SysAnnouncement();
                    sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                    sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                    sysAnnouncement.setUserIds(map.get("name") + ",");    //指定用户
                    sysAnnouncement.setTitile("您有一条请假申请待审批");
                    sysAnnouncement.setSendTime(new Date());        //发布时间
                    sysAnnouncement.setPriority(zsSqLeave.getEmergencyLevel());     //紧急程度
                    sysAnnouncement.setMsgType("USER");             //指定用户
                    sysAnnouncement.setMsgCategory("2");
                    sysAnnouncementService.saveAnnouncement(sysAnnouncement);
                    //移动端消息提醒

                    try {
                        SysUser receiver = sysUserMapper.selectById(guocheng.getApprovalPerson());
                        RTXConfig.sendNotify(receiver.getActivitiSync(), "请假申请", "您有一条请假申请待审批");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        WebSocketServer.SendMessage("您有一条请假申请待审批", map.get("name"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
                zsLeaveGuochengMapper.insert(zsLeaveGuocheng);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ZsSqLeave> findByIds(List<String> list) {
        List<ZsSqLeave> zsSqLeaveList = new ArrayList<>();
        for (String id: list) {
            ZsSqLeave sqLeave = zsSqLeaveMapper.findById(id);
            zsSqLeaveList.add(sqLeave);
        }
        return zsSqLeaveList;
    }

    @Override
    public List<ZsSqLeave> findAll(ZsSqLeave zsSqLeave) {
        return zsSqLeaveMapper.findAll(zsSqLeave);
    }


    public List<ZsSqLeave> getReceiver(List<ZsSqLeave> list) {
        for (ZsSqLeave zsSqLeave : list) {
            List<Map<String, String >> receiver = new ArrayList<>();
            List<ZsLeaveGuocheng> guochengs = zsLeaveGuochengMapper.findAllUser(zsSqLeave.getId());
            for (ZsLeaveGuocheng guocheng : guochengs) {
                String  realName = sysUserDepartMapper.selectRealNameById(guocheng.getApprovalPerson());
                Map<String, String > person = new HashMap<>();
                person.put("key", guocheng.getNumber());
                person.put("name", guocheng.getApprovalPerson());
                person.put("realName", realName);
                receiver.add(person);
            }
            zsSqLeave.setReceiver(receiver);
        }
        return list;
    }


    /*
     * 假期计算
     * */
    public int workDays(String strStartDate, String strEndDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        try {
            cl1.setTime(df.parse(strStartDate));
            cl2.setTime(df.parse(strEndDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            /*if (cl1.get(Calendar.DAY_OF_WEEK) != 7 && cl1.get(Calendar.DAY_OF_WEEK) != 1) {
                count++;
            }*/
            count++;
            cl1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return count;

    }

}
