package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsSqBack;

import java.util.Map;

/**
 * <p>
 * 销假
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-18
 * */
public interface ZsSqBackService extends IService<ZsSqBack> {

    ZsSqBack selectById(String id) throws Exception;

    IPage<ZsSqBack> backApplyList(String emergencyLevel, Page<ZsSqBack> page, String username);

    Map formShow(String id);

    IPage<ZsSqBack> personnelList(Page<ZsSqBack> page);

    IPage<ZsSqBack> allowList(String realname, Page<ZsSqBack> page, String username);

    void updateStatus(ZsSqBack zsSqBack);

    void add(ZsSqBack zsSqBack) throws Exception;
}
