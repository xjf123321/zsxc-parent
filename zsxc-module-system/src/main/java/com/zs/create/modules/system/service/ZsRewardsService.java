package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsRewards;

import java.util.List;

public interface ZsRewardsService extends IService<ZsRewards> {
    List<ZsRewards> select(String userId);

    void saveRecords(List<ZsRewards> list);

    void deleteRecords(String id);
}
