package com.zs.create.modules.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsSqLeave;

import java.util.List;
import java.util.Map;

public interface ZsSqLeaveTwoService extends IService<ZsSqLeave> {
    IPage<ZsSqLeave> findList(Page<ZsSqLeave> page, ZsSqLeave zsSqLeave);

    void add(ZsSqLeave zsSqLeave) throws Exception;

    IPage<ZsSqLeave> leaveApplyList(Page<ZsSqLeave> page, String emergencyLevel, String title, String username);

    void updateState(ZsSqLeave zsSqLeave);

    void disagree(ZsSqLeave zsSqLeave);

    ZsSqLeave findById(String id);

    Map formShow(String id);

    IPage<ZsSqLeave> haveDoneList(Page<ZsSqLeave> page, String username);

    IPage<ZsSqLeave> leaveList(Page<ZsSqLeave> page, ZsSqLeave zsSqLeave);

    void edit(ZsSqLeave zsSqLeave);

    List<ZsSqLeave> findByIds(List<String> list);

    List<ZsSqLeave> findAll(ZsSqLeave zsSqLeave);

}
