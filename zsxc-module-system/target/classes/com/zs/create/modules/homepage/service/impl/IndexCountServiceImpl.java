package com.zs.create.modules.homepage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.homepage.service.IndexCountService;
import com.zs.create.modules.process.mapper.ZsLeaveGuochengMapper;
import com.zs.create.modules.process.mapper.ZsMeettingGuochengMapper;
import com.zs.create.modules.process.service.ZsSqLeaveTwoService;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ZsDocRecordService;
import com.zs.create.modules.system.service.ZsSqCarService;
import com.zs.create.modules.system.service.ZsSqNoticeService;
import com.zs.create.modules.system.service.ZsSqRoomService;
import com.zs.create.modules.system.vo.ZsEmailVO;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class IndexCountServiceImpl implements IndexCountService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private ZsDocRecordMapper zsDocRecordMapper;

    @Autowired
    private ZsSqNoticeMapper zsSqNoticeMapper;

    @Autowired
    private ZsSqLeaveMapper zsSqLeaveMapper;

    @Autowired
    private ZsSqBackMapper zsSqBackMapper;

    @Autowired
    private ZsSqCarMapper zsSqCarMapper;

    @Autowired
    private ZsSqRoomMapper zsSqRoomMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Autowired
    private IndexCountMapper indexCountMapper;

    @Autowired
    private ZsCarGuochengMapper zsCarGuochengMapper;

    @Autowired
    private ZsMeettingGuochengMapper zsMeettingGuochengMapper;

    @Autowired
    private ZsLeaveGuochengMapper zsLeaveGuochengMapper;


    @Override
    public Boolean selectRole() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        Boolean flag = false;
        for (int i = 0; i < roleList.size(); i++) {
            if (roleList.get(i).getRoleCode().equals("PTRY")) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public Map<String, Long> indexCount() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, Long> map = new HashMap<>();
        Long docCount = zsDocRecordMapper.docCount(sysUser.getId());
        Long noticeCount = zsSqNoticeMapper.noticeCont(sysUser.getId());
        Long leaveCount = zsSqLeaveMapper.leaveCount(sysUser.getId());
        Long backCount = zsSqBackMapper.backCount(sysUser.getId());
        Long carCount = zsSqCarMapper.carCount(sysUser.getId());
        Long meettingCount = zsSqRoomMapper.roomCount(sysUser.getId());
        map.put("doc", docCount);
        map.put("notice", noticeCount);
        map.put("leave", leaveCount);
        map.put("back", backCount);
        map.put("car", carCount);
        map.put("room", meettingCount);
        return map;
    }

    @Override
    public Map selectSecrecy() {
        Map map = new HashMap();
        List<SysUser> list = new ArrayList<>();
        String roleId = sysRoleMapper.selectRoleId("CO");
        List<String> userIdlist = sysUserRoleMapper.selectId(roleId);
        for (int i = 0; i < userIdlist.size(); i++) {
            String username = sysUserMapper.selectById(userIdlist.get(i)).getRealname();
            SysUser sysUser = new SysUser();
            sysUser.setId(userIdlist.get(i));
            sysUser.setRealname(username);
            list.add(sysUser);
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List haveDone() {
        List list = new ArrayList();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Done> dataList1 = zsSqCarMapper.done(sysUser.getId());
        List<Done> dataList2 = zsSqRoomMapper.done(sysUser.getId());
        List<Done> dataList3 = zsSqLeaveMapper.done(sysUser.getId());
        methodTitle(dataList1);
        methodTitle(dataList2);
        methodTitle(dataList3);

        List<Done> dataList4 = zsDocRecordMapper.done(sysUser.getId());
        List<Done> dataList5 = zsSqNoticeMapper.done(sysUser.getId());
        methodApply(dataList4);
        methodApply(dataList5);

        method(dataList1,list);
        method(dataList2,list);
        method(dataList3,list);
        method(dataList4,list);
        method(dataList5,list);
        Collections.sort(list, new Comparator<Done>() {

            @Override
            public int compare(Done o1, Done o2) {
                if (o1.getCreateTime().compareTo(o2.getCreateTime()) > 0) {
                    return -1;
                } else if (o1.getCreateTime().compareTo(o2.getCreateTime()) < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return list;
    }

    @Override
    public IPage<Done> queryDone(Page<Done> page,String applyer) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Done> list = new ArrayList<>();
        List<Done> list1 = indexCountMapper.queryDone(sysUser.getId());
        List<Done> list2 = indexCountMapper.query(sysUser.getId());
        List<String> carIds = zsCarGuochengMapper.selectCarId(sysUser.getId());
        List<String> roomIds = zsMeettingGuochengMapper.selectRoomIds(sysUser.getId());
        List<String> leaveIds = zsLeaveGuochengMapper.selectLeaveIds(sysUser.getId());
        List<Done> dataList4 = zsDocRecordMapper.done(sysUser.getId());
        List<Done> dataList5 = zsSqNoticeMapper.done(sysUser.getId());
        for (int i = 0; i < carIds.size(); i++) {
            Done done = zsSqCarMapper.selectBy(carIds.get(i));
            if (done != null) {
                if (done.getStatus().equals("3") || done.getStatus().equals("4")) {
                    done.setStatus("1");
                }
                list.add(done);
            }
        }
        for (int i = 0; i < roomIds.size(); i++) {
            Done done = zsSqRoomMapper.selectBy(roomIds.get(i));
            if (done != null) {
                if (done.getStatus().equals("1")) {
                    done.setStatus("0");
                }else if (done.getStatus().equals("2") || done.getStatus().equals("3") || done.getStatus().equals("6") || done.getStatus().equals("7")) {
                    done.setStatus("1");
                }else if (done.getStatus().equals("4") || done.getStatus().equals("5")) {
                    done.setStatus("2");
                }
                list.add(done);
            }
        }
        for (int i = 0; i < leaveIds.size(); i++) {
            Done done = zsSqLeaveMapper.selectBy(leaveIds.get(i));
            if (done != null) {
                list.add(done);
            }
        }
        if (list1.size() != 0) {
            method(list1,list);
        }
        if (list2.size() != 0) {
            method(list2,list);
        }
        method(dataList4,list);
        method(dataList5,list);
        Collections.sort(list, new Comparator<Done>() {

            @Override
            public int compare(Done o1, Done o2) {
                if (o1.getCreateTime().compareTo(o2.getCreateTime()) > 0) {
                    return -1;
                } else if (o1.getCreateTime().compareTo(o2.getCreateTime()) < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        if (applyer != null) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getApplyer().equals(applyer)) {
                    list.remove(i);
                    i--;
                }
            }
        }
        page.setTotal(list.size());
        page.setRecords(list);
        return page;
    }


    public void method(List dataList,List list){
        for (int i = 0; i < dataList.size(); i++) {
            list.add(dataList.get(i));
        }
    }

    public void methodTitle(List<Done> list){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTitle("");
            if (list.get(i).getStatus().equals("1")) {
                if (list.get(i).getType().equals("0")){
                    list.get(i).setStatus("同意");
                }else if (list.get(i).getType().equals("1")){
                    list.get(i).setStatus("同意");
                }else if (list.get(i).getType().equals("2")){
                    list.get(i).setStatus("同意");
                }
            } else if (list.get(i).getStatus().equals("2")){
                if (list.get(i).getType().equals("0")){
                    list.get(i).setStatus("不同意");
                }else if (list.get(i).getType().equals("1")){
                    list.get(i).setStatus("同意");
                }else if (list.get(i).getType().equals("2")){
                    list.get(i).setStatus("不同意");
                }
            } else if (list.get(i).getStatus().equals("4")){
                if (list.get(i).getType().equals("1")){
                    list.get(i).setStatus("不同意");
                }
            } else if (list.get(i).getStatus().equals("5")){
                if (list.get(i).getType().equals("1")){
                    list.get(i).setStatus("不同意");
                }
            }
            if (list.get(i).getType().equals("0")) {
                list.get(i).setType("车辆申请");
            }else if (list.get(i).getType().equals("1")){
                list.get(i).setType("会议室申请");
            }else if (list.get(i).getType().equals("2")) {
                list.get(i).setType("请假申请");
            }
        }
    }

    public void methodApply(List<Done> list){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setType("");
            if (list.get(i).getStatus().equals("1")) {
                list.get(i).setStatus("同意");
            } else {
                list.get(i).setStatus("不同意");
            }
        }
    }
}
