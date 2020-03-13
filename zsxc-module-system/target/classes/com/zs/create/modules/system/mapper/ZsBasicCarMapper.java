package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsBasicCar;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/*
* 车辆信息表接口
* */
public interface ZsBasicCarMapper extends BaseMapper<ZsBasicCar> {

    List<ZsBasicCar> countStatus();

    Long count(String vehicleNumber);

    Long countPlateNumber(String plateNumber);

    void updateCarStatus(@Param("id") String plateNumber, @Param("status") String status);

    String selectPlateNumber(@Param("id") String id);

    void updateCarLock(@Param("id") String id, @Param("status") String status);

    void updateTime(@Param("id") String id,@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ZsBasicCar> selectCar();

    void cleanTime(@Param("id") String id);

    void updateCar(@Param("list") List<ZsBasicCar> list);

    String selectIdByNumber(@Param("plateNumber") String plateNumber);

    void updateBasicCar(@Param("id") String id, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<String> selectAll();
}
