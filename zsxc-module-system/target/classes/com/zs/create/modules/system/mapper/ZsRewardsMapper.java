package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsRewards;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZsRewardsMapper extends BaseMapper<ZsRewards> {
    List<ZsRewards> select(@Param("userId") String userId);

    void deleteRecords(@Param("id") String id);

    void updateRewards(@Param("list") List<ZsRewards> list);
}
