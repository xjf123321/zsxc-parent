package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.ZsSqCar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZsSqCarTwoMapper extends BaseMapper<ZsSqCar> {

    List<ZsSqCar> queryLeaderCar(@Param("approvalPerson")String approvalPerson, @Param("emergencyLevel")String emergencyLevel,
                                           @Param("title")String title, @Param("username")String username,
                                           @Param("firstIndex")int firstIndex, @Param("lastIndex")int lastIndex);

    void updateStatus(@Param("id") String id, @Param("status") String status);

    String selectPlateNumber(@Param("id") String id);

    IPage<ZsSqCar> findList(@Param("page")Page<ZsSqCar> page, @Param("zsSqCar")ZsSqCar zsSqCar, @Param("userId")String userId);

    void updateCar(ZsSqCar zsSqCar);

    String selectEmergencyLevel(@Param("id") String id);
}
