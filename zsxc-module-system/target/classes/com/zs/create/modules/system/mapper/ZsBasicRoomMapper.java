package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsBasicRoom;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ZsBasicRoomMapper extends BaseMapper<ZsBasicRoom> {
    Long count(String roomNumber);

    List<ZsBasicRoom> countStatus();

    void updateMeettingStatus(@Param("id") String id, @Param("status") String status);

    void cancel(@Param("id") String roomName);

    List<ZsBasicRoom> selectRoom();

    void updateTime(@Param("id") String id, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    void cleanTime(@Param("id") String id);

    String selectRoomId(String meettingRoomName);

    String selectRoomName(@Param("id") String id);
}
