package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsOperationalSituation;

import java.util.List;

public interface ZsOperationalSituationService extends IService<ZsOperationalSituation> {
    List<ZsOperationalSituation> select(String userId);

    void saveFight(List<ZsOperationalSituation> list);

    void deleteOperational(String id);

}
