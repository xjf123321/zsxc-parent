package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsTrainingExperience;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZsTrainingExperienceMapper extends BaseMapper<ZsTrainingExperience> {
    List<ZsTrainingExperience> select(@Param("userId") String userId);

    void deleteTraining(@Param("id") String id);

    void updateTraining(@Param("list") List<ZsTrainingExperience> list);
}
