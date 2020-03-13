package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsSqCar;

import java.util.Map;

public interface ZsSqCarTwoService extends IService<ZsSqCar> {
    void add(ZsSqCar zsSqCar) throws Exception;

    Map<String, Object> carApplyList(String emergencyLevel, String title, String username, Integer pageNo, Integer pageSize);

    void agree(ZsSqCar zsSqCar);

    void reject(ZsSqCar zsSqCar);

    void reapply(ZsSqCar zsSqCar);

    IPage<ZsSqCar> findList(Page<ZsSqCar> page, ZsSqCar zsSqCar);

    void updateCar(ZsSqCar zsSqCar);

    IPage<ZsSqCar> haveDoneList(Page<ZsSqCar> page, String username);

    Map formShow(String id);
}
