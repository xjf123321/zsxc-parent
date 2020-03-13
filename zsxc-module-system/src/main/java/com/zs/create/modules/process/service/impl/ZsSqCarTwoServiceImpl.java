package com.zs.create.modules.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import com.zs.create.modules.system.service.ZsSqCarTwoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ZsSqCarTwoServiceImpl extends ServiceImpl<ZsSqCarMapper, ZsSqCar> implements ZsSqCarTwoService {

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private ZsSqCarMapper zsSqCarMapper;

    @Autowired
    private ZsCarGuochengMapper zsCarGuochengMapper;

    @Autowired
    private ZsBasicCarMapper zsBasicCarMapper;

    @Autowired
    private ZsSqCarTwoMapper zsSqCarTwoMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    @Transactional
    public void add(ZsSqCar zsSqCar) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);   //当前登陆人部门id
        try {
            zsSqCar.setDelFlag("0");
            zsSqCar.setStatus("0");
            zsSqCar.setCreateTime(new Date());
            zsSqCar.setApplyType("0");
            zsSqCar.setUsername(sysUser.getRealname());
            zsSqCar.setUserId(sysUser.getId());
            zsSqCar.setApplyerDept(deptId);
            zsSqCarMapper.add(zsSqCar);
            //车辆过程表中插数据
            //车辆申请信息插入
            ZsCarGuocheng zsCar = new ZsCarGuocheng();
            zsCar.setCarId(zsSqCar.getId());
            zsCar.setUserName(zsSqCar.getUsername());
            zsCar.setUserId(userId);
            zsCar.setState("3");
            zsCar.setCreateTime(new Date());
            zsCar.setPlayName("车辆申请提交");
            zsCarGuochengMapper.insert(zsCar);
            //车辆审批信息插入
            for (Map<String, String> map : zsSqCar.getReceiver()) {
                ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
                zsCarGuocheng.setApprovalPerson(map.get("name"));
                zsCarGuocheng.setNumber(map.get("key"));
                zsCarGuocheng.setCarId(zsSqCar.getId());
                zsCarGuocheng.setState("0");
                zsCarGuocheng.setCreateTime(new Date());
                zsCarGuocheng.setApprovalOpinion(zsSqCar.getApprovalOpinion());
                if (map.get("key").equals("1")) {
                    zsCarGuocheng.setFlag("1");
                    //pc端消息提醒
                    try {
                        SysAnnouncement sysAnnouncement = new SysAnnouncement();
                        sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                        sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                        sysAnnouncement.setUserIds(map.get("name")+",");    //指定用户
                        sysAnnouncement.setTitile("您有一条车辆申请待审批");
                        sysAnnouncement.setSendTime(new Date());        //发布时间
                        sysAnnouncement.setPriority(zsSqCar.getEmergencyLevel());     //紧急程度
                        sysAnnouncement.setMsgType("USER");             //指定用户
                        sysAnnouncement.setMsgCategory("2");
                        sysAnnouncementService.saveAnnouncement(sysAnnouncement);

                        SysUser receiver = sysUserMapper.selectById(map.get("name"));
                        RTXConfig.sendNotify(receiver.getActivitiSync(), "车辆申请", "您有一条车辆申请待审批");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    zsCarGuocheng.setFlag("-2");
                }

                zsCarGuochengMapper.insert(zsCarGuocheng);
            }
            //修改车的状态
            String[] arr = zsSqCar.getPlateNumber().split(",");
            for (int i = 0; i < arr.length; i++) {
                zsBasicCarMapper.updateCarLock(arr[i], "5");
                //zsBasicCarMapper.updateTime(arr[i],zsSqCar.getOutDate(),zsSqCar.getInDate());
            }
        } catch (Exception e) {
            throw new Exception("SaveConferenceRoom Error");
        }
        //pad端提醒信息
        for (Map<String, String> map : zsSqCar.getReceiver()) {
            if ("1".equals(map.get("key"))) {
                try {
                    WebSocketServer.SendMessage("您有一条车辆申请待审批", map.get("name"));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Map<String, Object> carApplyList(String emergencyLevel, String title, String username, Integer pageNo, Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        List<String> carIdList = zsCarGuochengMapper.selectId(userId, emergencyLevel, title, username);
        Long total = Long.valueOf(carIdList.size());
        List<ZsSqCar> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsSqCarTwoMapper.queryLeaderCar(userId, emergencyLevel, title, username, firstIndex, lastIndex);
            for (int i = 0; i < list.size(); i++) {
                String[] arr = list.get(i).getPlateNumber().split(",");
                String carName = "";
                for (int j = 0; j < arr.length; j++) {
                    String plateNumber = zsBasicCarMapper.selectPlateNumber(arr[j]);
                    if (j == 0) {
                        carName = carName + plateNumber;
                    }else {
                        carName =carName + "," + plateNumber;
                    }
                }
                list.get(i).setCarName(carName);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    @Override
    @Transactional
    public void agree(ZsSqCar zsSqCar) {
        ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsCarGuocheng.setCarId(zsSqCar.getId());
        zsCarGuocheng.setPlayName("审批同意");      //操作名称
        zsCarGuocheng.setCreateTime(new Date());
        zsCarGuochengMapper.updateCarGuochengStatus(sysUser.getId(), zsCarGuocheng.getCarId(), zsCarGuocheng.getPlayName(),
                                                    zsSqCar.getApprovalOpinion(), sysUser.getRealname(), zsCarGuocheng.getCreateTime(), zsSqCar.getRemarks());
        List<String> list = zsCarGuochengMapper.selectCarGuochengNumber(zsCarGuocheng.getCarId());
        List<Integer> integerList = stringToInt(list);
        int max = Max(integerList);
        String number = zsCarGuochengMapper.selectCarGuocheng(sysUser.getId(), zsCarGuocheng.getCarId());
        if (Integer.parseInt(number) == max) {
            zsSqCarTwoMapper.updateStatus(zsCarGuocheng.getCarId(),"1");
            String plateNumber = zsSqCarTwoMapper.selectPlateNumber(zsCarGuocheng.getCarId());
            String[] arr = plateNumber.split(",");
            for (int i = 0; i < arr.length; i++) {
                zsBasicCarMapper.updateCarStatus(arr[i],"4");
            }
        }else {
            int number1 = Integer.parseInt(number) + 1;
            zsCarGuochengMapper.updateCarGuochengNumber(Integer.toString(number1), zsCarGuocheng.getCarId());
            String approvalPerson = zsCarGuochengMapper.selectApprovalPerson(Integer.toString(number1), zsCarGuocheng.getCarId());
            String emergencyLevel = zsSqCarTwoMapper.selectEmergencyLevel(zsCarGuocheng.getCarId());
            SysAnnouncement sysAnnouncement = new SysAnnouncement();
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
            sysAnnouncement.setUserIds(approvalPerson+",");    //指定用户
            sysAnnouncement.setTitile("您有一条车辆申请待审批");
            sysAnnouncement.setSendTime(new Date());        //发布时间
            sysAnnouncement.setPriority(emergencyLevel);     //紧急程度
            sysAnnouncement.setMsgType("USER");             //指定用户
            sysAnnouncement.setMsgCategory("2");
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);
            //移动端消息提醒

            try {
                SysUser receiver = sysUserMapper.selectById(approvalPerson);
                RTXConfig.sendNotify(receiver.getActivitiSync(), "车辆申请", "您有一条车辆申请待审批");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                WebSocketServer.SendMessage("您有一条车辆申请待审批", approvalPerson);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    @Transactional
    public void reject(ZsSqCar zsSqCar) {
        ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsCarGuocheng.setCarId(zsSqCar.getId());
        zsCarGuocheng.setPlayName("不同意");      //操作名称
        zsCarGuocheng.setCreateTime(new Date());
        zsCarGuocheng.setUserId(sysUser.getId());
        zsCarGuochengMapper.updateNotCarGuochengStatus(sysUser.getId(), zsCarGuocheng.getCarId(), zsCarGuocheng.getPlayName(),
                zsSqCar.getApprovalOpinion(), zsCarGuocheng.getCreateTime(), sysUser.getRealname());
        zsSqCarTwoMapper.updateStatus(zsCarGuocheng.getCarId(),"2");
        String plateNumber = zsSqCarTwoMapper.selectPlateNumber(zsCarGuocheng.getCarId());
        String[] arr = plateNumber.split(",");
        for (int i = 0; i < arr.length; i++) {
            zsBasicCarMapper.updateCarStatus(arr[i],"0");
            zsBasicCarMapper.cleanTime(arr[i]);
        }
    }

    @Override
    @Transactional
    public void reapply(ZsSqCar zsSqCar) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        //清除过程表中的数据
        zsCarGuochengMapper.clean(zsSqCar.getId());
        zsCarGuochengMapper.updateFlag(zsSqCar.getId());
        zsSqCar.setDelFlag("0");
        zsSqCar.setStatus("0");
        zsSqCar.setCreateTime(new Date());
        zsSqCar.setApplyType("0");
        zsSqCar.setUsername(sysUser.getRealname());
        zsSqCar.setUserId(sysUser.getId());
        //zsSqCarMapper.add(zsSqCar);
        //zsSqCarTwoMapper.updateCar(zsSqCar);
        List<Map<String, String>> receiver = zsSqCar.getReceiver();
        zsSqCar.setReceiver(null);
        zsSqCar.setCarName(null);
        zsSqCarTwoMapper.updateById(zsSqCar);
        String[] arr = zsSqCarTwoMapper.selectPlateNumber(zsSqCar.getId()).split(",");
        for (int i = 0; i < arr.length; i++) {
            zsBasicCarMapper.updateCarStatus(arr[i],"5");
            //zsBasicCarMapper.updateTime(arr[i],zsSqCar.getOutDate(),zsSqCar.getInDate());
        }
        //往过程表中插入数据
        //车辆申请信息插入
        ZsCarGuocheng zsCar = new ZsCarGuocheng();
        zsCar.setCarId(zsSqCar.getId());
        zsCar.setUserName(zsSqCar.getUsername());
        zsCar.setUserId(userId);
        zsCar.setState("3");
        zsCar.setCreateTime(new Date());
        zsCar.setPlayName("车辆申请提交");
        zsCarGuochengMapper.insert(zsCar);
        //车辆审批信息插入
        for (int i = 0; i <receiver.size(); i++) {
            ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
            Map<String,String> map = receiver.get(i);
            if (map.get("key").equals("1")) {
                zsCarGuocheng.setFlag("1");
                //pc端消息提醒
                SysAnnouncement sysAnnouncement = new SysAnnouncement();
                sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
                sysAnnouncement.setUserIds(map.get("name")+",");    //指定用户
                sysAnnouncement.setTitile("您有一条车辆申请待审批");
                sysAnnouncement.setSendTime(new Date());        //发布时间
                sysAnnouncement.setPriority(zsSqCar.getEmergencyLevel());     //紧急程度
                sysAnnouncement.setMsgType("USER");             //指定用户
                sysAnnouncement.setMsgCategory("2");
                sysAnnouncementService.saveAnnouncement(sysAnnouncement);

                try {
                    SysUser receivers = sysUserMapper.selectById(map.get("name"));
                    RTXConfig.sendNotify(receivers.getActivitiSync(), "车辆申请", "您有一条车辆申请待审批");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //移动端消息提醒
                try {
                    WebSocketServer.SendMessage("您有一条车辆申请待审批", map.get("name"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                zsCarGuocheng.setFlag("-2");
            }
            zsCarGuocheng.setApprovalPerson(map.get("name"));
            zsCarGuocheng.setNumber(map.get("key"));
            zsCarGuocheng.setCarId(zsSqCar.getId());
            zsCarGuocheng.setUserName(zsSqCar.getUsername());
            zsCarGuocheng.setState(zsSqCar.getStatus());
            zsCarGuocheng.setCreateTime(new Date());
            zsCarGuocheng.setPlayName("车辆申请提交");
            zsCarGuocheng.setApprovalOpinion(zsSqCar.getApprovalOpinion());
            zsCarGuochengMapper.insert(zsCarGuocheng);
        }
    }

    @Override
    public IPage<ZsSqCar> findList(Page<ZsSqCar> page, ZsSqCar zsSqCar) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        IPage<ZsSqCar> pageList = zsSqCarTwoMapper.findList(page, zsSqCar, sysUser.getId());
        List<ZsSqCar> receiver = getReceiver(pageList.getRecords());
        List<ZsSqCar> list = pageList.getRecords();
        for (int i = 0; i < list.size(); i++) {
            String[] arr = list.get(i).getPlateNumber().split(",");
            String carName = "";
            for (int k = 0; k < arr.length; k++) {
                String plateNumber = zsBasicCarMapper.selectPlateNumber(arr[k]);
                if (k == 0){
                    carName = plateNumber;
                }else {
                    carName = carName + "," + plateNumber;
                }
            }
            list.get(i).setCarName(carName);
        }
        pageList.setRecords(receiver);
        return pageList;
    }

    @Override
    @Transactional
    public void updateCar(ZsSqCar zsSqCar) {
        zsSqCarTwoMapper.updateCar(zsSqCar);
    }

    @Override
    public IPage<ZsSqCar> haveDoneList(Page<ZsSqCar> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = zsSqCarMapper.haveDoneCount(sysUser.getId(), username);
        List<ZsSqCar> bpmnWorkIPage = zsSqCarMapper.haveDoneList(page, sysUser.getId(), username);
        for (ZsSqCar zsSqCar : bpmnWorkIPage) {
            String plateNumber = "";
            String[] carIds = zsSqCar.getPlateNumber().split(",");
            for (String id : carIds) {
                ZsBasicCar car = zsBasicCarMapper.selectById(id);
                if (car != null) {
                    plateNumber += car.getPlateNumber() + " ";
                }
            }
            zsSqCar.setPlateNumber(plateNumber);
        }
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    public Map formShow(String id) {
        Map map = new HashMap();
        ZsSqCar zsSqCar = zsSqCarMapper.formShow(id);
        zsSqCar.setUseDept(sysDepartMapper.selectDeptName(zsSqCar.getUseDept()));
        String[] split = zsSqCar.getPlateNumber().split(",");
        String plateNumber = "";
        for (int i = 0; i < split.length; i++) {
            plateNumber += zsBasicCarMapper.selectPlateNumber(split[i]) + " ";
        }
        zsSqCar.setPlateNumber(plateNumber);
        List<ZsCarGuocheng> zsCarGuochengList = zsCarGuochengMapper.formShow(id);
        map.put("formData", zsSqCar);
        map.put("processList", zsCarGuochengList);
        return map;
    }

    /*
    * list集合中string类型转换成int
    * */
    public List<Integer> stringToInt(List<String> list){
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            integerList.add(Integer.parseInt(list.get(i)));
        }
        return integerList;
    }

    /*
    * 取出list集合中最大的值
    * */
    public int Max(List<Integer> list){
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) >= max) {
                max = list.get(i);
            }
        }
        return max;
    }


    public List<ZsSqCar> getReceiver(List<ZsSqCar> list) {
        for (ZsSqCar zsSqLeave : list) {
            List<Map<String, String >> receiver = new ArrayList<>();
            List<ZsCarGuocheng> guochengs = zsCarGuochengMapper.findAllUser(zsSqLeave.getId());
            for (ZsCarGuocheng guocheng : guochengs) {
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

}
