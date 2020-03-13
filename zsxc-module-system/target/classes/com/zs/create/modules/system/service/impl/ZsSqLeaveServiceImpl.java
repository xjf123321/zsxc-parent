package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import com.zs.create.modules.process.mapper.ZsLeaveGuochengMapper;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ZsSqLeaveService;
import com.zs.create.modules.workflow.instance.service.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@Service
@Slf4j
public class ZsSqLeaveServiceImpl extends ServiceImpl<ZsSqLeaveMapper, ZsSqLeave> implements ZsSqLeaveService {

    @Autowired
    private ZsSqLeaveMapper zsSqLeaveMapper;

    @Autowired
    private ZsLeaveGuochengMapper zsLeaveGuochengMapper;

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
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ZsRemindRecordMapper zsRemindRecordMapper;

    @Override
    @Transactional
    public void add(ZsSqLeave zsSqLeave) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);   //当前登陆人部门id
        try {
            zsSqLeave.setDelFlag("0");
            zsSqLeave.setStatus("0");
            zsSqLeave.setCreateTime(new Date());
            zsSqLeave.setApplyType("2");
            zsSqLeave.setUsername(sysUser.getRealname());
            zsSqLeave.setUserId(sysUser.getId());
            zsSqLeave.setApplyerDept(deptId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int count = workDays(sdf.format(zsSqLeave.getStartDate()), sdf.format(zsSqLeave.getEndDate()));
            zsSqLeave.setNumber(String.valueOf(count));
            zsSqLeaveMapper.insert(zsSqLeave);
            //请假过程表插入数据
            ZsLeaveGuocheng zsLeaveGuocheng = new ZsLeaveGuocheng();
            zsLeaveGuocheng.setLeaveId(zsSqLeave.getId());
            zsLeaveGuocheng.setCreateTime(new Date());
            zsLeaveGuocheng.setState("0");
            //zsLeaveGuocheng.setUserId(sysUser.getId());
            zsLeaveGuocheng.setUserName(sysUser.getRealname());
            zsLeaveGuocheng.setPlayName("请假申请提交");
            zsLeaveGuochengMapper.insert(zsLeaveGuocheng);
        } catch (Exception e) {
            throw new Exception("SaveZsSqLeave Error");
        }
        //提醒信息
        List<String> usersId = sysUserDepartMapper.selectIdsByDeptId(deptId);
        for (String id: usersId) {
            try  {
                WebSocketServer.SendMessage("您有一条请假申请待审批", id);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ZsSqLeave selectById(String id) throws Exception {
        ZsSqLeave zsSqLeave = zsSqLeaveMapper.selectById(id);
        return zsSqLeave;
    }

    @Override
    public IPage<ZsSqLeave> leaveApplyList(IPage<ZsSqLeave> page, String emergencyLevel, String title, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        for (SysRole sysRole: roleList) {
            if (sysRole.getRoleCode().equals("BMJL")||sysRole.getRoleCode().equals("KSLD")) {   //判断当前用户是否包含BMJL(处室领导)角色或者包含KSLD(科室领导)角色
                String state = "0";     //领导待审批
                Long count = zsSqLeaveMapper.leaveApplyCount(deptId, state, emergencyLevel, title, username);
                List<ZsSqLeave> bpmnWorkIPage = zsSqLeaveMapper.leaveApplyList(page, deptId, state, emergencyLevel, title, username);
                page.setRecords(bpmnWorkIPage);
                page.setTotal(count);
                return page;
            }
        }
        Long count = new Long(0);
        int atr = 0;
        List<ZsSqLeave> bpmnWorkIPage = new ArrayList<ZsSqLeave>();
        List<SysDepart> depaerList = sysDepartMapper.listDeparts(sysUser.getId());
        if (depaerList.size() != 0) {
            for (SysDepart sysDepart:depaerList) {
                String state = "1";     //分领导待审批
                List<ZsSqLeave> forBpmnWorkIPage = zsSqLeaveMapper.leaveApplyList(page, sysDepart.getId(), state, emergencyLevel, title, username);
                if (forBpmnWorkIPage.size() != 0) {
                    for (ZsSqLeave zsSqLeave:forBpmnWorkIPage) {
                        bpmnWorkIPage.add(atr++, zsSqLeave);
                        count++;
                    }
                }
            }
        }
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    @Transactional
    public void updateStatus(ZsSqLeave zsSqLeave) {
        zsSqLeaveMapper.updateStatus(zsSqLeave.getId(), zsSqLeave.getStatus());
    }

    @Override
    @Transactional
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
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = zsSqLeaveMapper.haveDoneCount(sysUser.getId(), username);
        List<ZsSqLeave> bpmnWorkIPage = zsSqLeaveMapper.haveDoneList(page, sysUser.getId(), username);
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    public Map<String, Object> queryZsSqLeave(ZsSqLeave zsSqLeave, Integer pageNo, Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Long total  = zsSqLeaveMapper.queryZsSqLeaveCount(sysUser.getId(), zsSqLeave.getStartDate(), zsSqLeave.getEndDate());
        List<ZsSqNotice> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsSqLeaveMapper.queryZsSqLeave(sysUser.getId(), zsSqLeave.getStartDate(), zsSqLeave.getEndDate(), firstIndex, lastIndex);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    @Override
    public Boolean listDeparts() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        for (SysRole sysRole: roleList) {
            if (sysRole.getRoleCode().equals("BMJL")) {   //判断当前用户是否包含BMJL角色
                return true;
            }
        }
        return false;
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
