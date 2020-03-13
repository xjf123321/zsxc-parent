package com.zs.create.modules.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZsLeaveGuochengMapper extends BaseMapper<ZsLeaveGuocheng> {

    List<ZsLeaveGuocheng> formShow(String carId);

    List<ZsLeaveGuocheng> findAllUser(String id);

    void updateFlag(String id);

    List<String> selectLeaveIds(@Param("userId") String userId);
}
