package com.zs.create.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import com.zs.create.modules.system.entity.ZsSqLeave;

public interface ZsLeaveGuochengService extends IService<ZsLeaveGuocheng> {

    /**
     * 会议过程表插入数据
     * @param zsSqLeave
     */
    void add(ZsSqLeave zsSqLeave);

    void addAndTx(ZsSqLeave zsSqLeave);
}
