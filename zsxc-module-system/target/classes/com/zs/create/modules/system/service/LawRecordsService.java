package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.LawRecords;
import com.zs.create.modules.system.entity.SysUser;

public interface LawRecordsService extends IService<LawRecords> {
    void editUserLaw(SysUser user, LawRecords lawRecords);
}
