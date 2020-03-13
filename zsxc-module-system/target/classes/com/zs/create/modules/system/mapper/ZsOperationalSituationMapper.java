package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsOperationalSituation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZsOperationalSituationMapper extends BaseMapper<ZsOperationalSituation> {

    List<ZsOperationalSituation> select(@Param("userId") String userId);

    void deleteOperational(@Param("id") String id);

    void updateOperational(@Param("list") List<ZsOperationalSituation> list);
}
