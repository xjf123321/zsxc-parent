package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsBasicRoom;
import com.zs.create.modules.system.entity.ZsSqRoom;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会议室信息表 服务类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-10
 */
public interface ZsBasicRoomService extends IService<ZsBasicRoom> {
    void add(ZsBasicRoom zsBasicRoom)throws Exception;

    Map<String, Integer> status();

    void updateStatus(ZsSqRoom zsSqRoom);


    ZsBasicRoom updateDept(ZsBasicRoom zsBasicRoom);

    void updateMeettingStatus(String meettingRoomName, String status);

    List<ZsBasicRoom> selectRoom(String startTime, String endTime);

    void cleanTime(String id);
}
