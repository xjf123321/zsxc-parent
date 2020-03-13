package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.FamilyLawRecords;
import com.zs.create.modules.system.entity.SysUser;

public interface FamilyLawRecordsService extends IService<FamilyLawRecords> {
    void editUserFamilyLaw(SysUser user, FamilyLawRecords familyLawRecords);
}
