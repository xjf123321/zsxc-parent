package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsCarGuocheng;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * <p>
 * 车辆过程表 mapper接口
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
public interface ZsCarGuochengMapper extends BaseMapper<ZsCarGuocheng> {
    List<ZsCarGuocheng> formShow(String carId);

    List<String> selectId(@Param("approvalPerson")String approvalPerson, @Param("emergencyLevel")String emergencyLevel,
                          @Param("title")String title, @Param("username")String username);

    void updateCarGuochengStatus(@Param("approvalPerson")String approvalPerson, @Param("carId")String carId, @Param("playName")String playName,
                                 @Param("approvalOpinion") String approvalOpinion, @Param("userName")String username,
                                 @Param("createTime") Date createTime, @Param("autograph")String autograph);

    String selectCarGuocheng(@Param("approvalPerson") String approvalPerson, @Param("carId")String carId);

    void updateCarGuochengNumber(@Param("number") String number, @Param("carId")String carId);

    List<String> selectCarGuochengNumber(@Param("carId") String carId);

    void updateNotCarGuochengStatus(@Param("approvalPerson")String approvalPerson, @Param("carId")String carId, @Param("playName")String playName,
                                    @Param("approvalOpinion") String approvalOpinion, @Param("createTime") Date createTime, @Param("userName")String username);

    void clean(@Param("carId") String carId);

    String selectApprovalPerson(@Param("number") String number, @Param("carId")String carId);

    List<ZsCarGuocheng> findAllUser(@Param("carId") String carId);

    void updateFlag(@Param("carId") String carId);

    void deleteCar(@Param("carId") String id);

    List<String> selectCarId(@Param("userId") String userId);
}
