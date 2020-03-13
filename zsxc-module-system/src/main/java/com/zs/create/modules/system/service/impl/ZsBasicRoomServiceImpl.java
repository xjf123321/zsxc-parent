package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsBasicRoom;
import com.zs.create.modules.system.entity.ZsSqRoom;
import com.zs.create.modules.system.mapper.SysDepartMapper;
import com.zs.create.modules.system.mapper.ZsBasicRoomMapper;
import com.zs.create.modules.system.service.ZsBasicRoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ZsBasicRoomServiceImpl extends ServiceImpl<ZsBasicRoomMapper, ZsBasicRoom> implements ZsBasicRoomService {

    @Autowired
    private ZsBasicRoomMapper zsBasicRoomMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;


    @Override
    @Transactional
    public void add(ZsBasicRoom zsBasicRoom) throws Exception {
        try {
            zsBasicRoom.setDelFlag("0");
            zsBasicRoom.setCreateTime(new Date());
            Subject subject = SecurityUtils.getSubject();
            LoginUser sysUser = (LoginUser) subject.getPrincipal();
            zsBasicRoom.setCreateId(sysUser.getUsername());
            zsBasicRoom.setApplyer(sysUser.getUsername());
            zsBasicRoom.setRealname(sysUser.getRealname());
            String deptName = sysDepartMapper.selectDeptName(zsBasicRoom.getDept());
            zsBasicRoom.setDeptName(deptName);
            zsBasicRoomMapper.insert(zsBasicRoom);
        } catch (Exception e) {
            throw new Exception("SaveConferenceRoom Error");
        }
    }

    @Override
    public Map<String, Integer> status() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<ZsBasicRoom> roomList = zsBasicRoomMapper.countStatus();
        int count0 = 0;
        int count1 = 0;
        for (int i = 0; i < roomList.size(); i++) {
            String status = roomList.get(i).getStatus();
            if (status.equals("0")) {
                count0++;
            } else if (status.equals("1")) {
                count1++;
            }
        }
        map.put("total", roomList.size());
        map.put("free", count0);
        map.put("use", count1);
        return map;
    }

    @Override
    @Transactional
    public void updateStatus(ZsSqRoom zsSqRoom) {
        ZsBasicRoom zsBasicRoom = new ZsBasicRoom();
        zsBasicRoom.setId(zsSqRoom.getMeettingRoomName());
        zsBasicRoom.setStatus("0");
        zsBasicRoomMapper.updateById(zsBasicRoom);
    }

    @Override
    @Transactional
    public ZsBasicRoom updateDept(ZsBasicRoom zsBasicRoom) {
        String deptName = sysDepartMapper.selectDeptName(zsBasicRoom.getDept());
        zsBasicRoom.setDeptName(deptName);
        return zsBasicRoom;
    }

    @Override
    public void updateMeettingStatus(String meettingRoomName, String status) {
        zsBasicRoomMapper.updateMeettingStatus(meettingRoomName,status);
    }

    @Override
    public List<ZsBasicRoom> selectRoom(String startTime, String endTime) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        List<ZsBasicRoom> list = zsBasicRoomMapper.selectRoom();
        for (int i = 0; i < list.size(); i++) {
            Date startTime1 = list.get(i).getStartTime();
            Date endTime1 = list.get(i).getEndTime();
            if (startTime1 != null && endTime1 != null) {
                try {
                    int date1 = formatter.parse(startTime).compareTo(endTime1);
                    int date2 = formatter.parse(endTime).compareTo(startTime1);
                    if (date1 == 1 || date2 == -1) {
                        continue;
                    } else {
                        list.remove(i);
                        i--;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public void cleanTime(String id) {
        zsBasicRoomMapper.cleanTime(id);
    }
}
