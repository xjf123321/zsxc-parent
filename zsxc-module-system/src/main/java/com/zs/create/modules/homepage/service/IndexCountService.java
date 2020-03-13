package com.zs.create.modules.homepage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.Done;

import java.util.List;
import java.util.Map;

public interface IndexCountService {
    Boolean selectRole();

    Map<String, Long> indexCount();

    Map selectSecrecy();

    List haveDone();

    IPage<Done> queryDone(Page<Done> page,String applyer);
}
