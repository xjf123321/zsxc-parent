package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zs.create.modules.system.entity.Done;
import com.zs.create.modules.system.entity.ZsSqCar;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车辆申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
public interface ZsSqCarMapper extends BaseMapper<ZsSqCar> {

    List<ZsSqCar> carApplyList(IPage<ZsSqCar> page, @Param("deptId") String deptId, @Param("emergencyLevel") String emergencyLevel, @Param("state") String state, @Param("title") String title, @Param("username") String username);

    Long carApplyCount(@Param("deptId") String deptId, @Param("emergencyLevel") String emergencyLevel, @Param("title") String title, @Param("username") String username);

    void updateStatus(@Param("id")String id, @Param("status")String status);

    List<ZsSqCar> vehicleOfficerList(IPage<ZsSqCar> page, @Param("applyer")String applyer);

    Long vehicleOfficerCount(@Param("applyer")String applyer);

    ZsSqCar formShow(String id);

    List<ZsSqCar> haveDoneList(IPage<ZsSqCar> page, @Param("userId") String userId, @Param("username") String username);

    Long haveDoneCount(@Param("userId") String userId, @Param("username") String username);

    void updateInDate(@Param("inDate") Date inDate, @Param("id") String id, @Param("status") String status);

    String selectPlateNumber(@Param("id") String id);

    void add(ZsSqCar zsSqCar);

    ZsSqCar selectCar(String id);

    ZsSqCar findById(String id);

    void updateFlag(@Param("id") String id);

    Long carCount(String userId);

    List<Done> done(String userId);

    List<ZsSqCar> select();

    List<ZsSqCar> selectAll();

    Done selectBy(@Param("id") String id);
}
