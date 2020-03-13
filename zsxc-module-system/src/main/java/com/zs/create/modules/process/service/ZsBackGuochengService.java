package com.zs.create.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.process.entity.ZsBackGuocheng;
import com.zs.create.modules.system.entity.ZsSqBack;

public interface ZsBackGuochengService extends IService<ZsBackGuocheng> {
    void addRecord(ZsSqBack zsSqBack);
}
