package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsTrainingExperience;

import java.util.List;

public interface ZsTrainingExperienceService extends IService<ZsTrainingExperience> {
    List<ZsTrainingExperience> select(String userId);

    void saveTraining(List<ZsTrainingExperience> list);

    void deleteTraining(String id);
}
