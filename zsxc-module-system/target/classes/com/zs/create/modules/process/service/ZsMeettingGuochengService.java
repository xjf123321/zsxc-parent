package com.zs.create.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.process.entity.ZsMeettingGuocheng;
import com.zs.create.modules.system.entity.ZsSqRoom;

/**
 * <p>
 * 会议审批记录
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-4
 */
public interface ZsMeettingGuochengService extends IService<ZsMeettingGuocheng> {
    void add(ZsMeettingGuocheng zsMeettingProcess, String emergencyLevel);

    void addProcess(ZsSqRoom zsSqRoom);

    void addProcessAndTx(ZsSqRoom zsSqRoom, String emergencyLevel);
}
