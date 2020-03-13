package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.FamilyLawRecords;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.mapper.FamilyLawRecordsMapper;
import com.zs.create.modules.system.service.FamilyLawRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FamilyLawRecordsServiceImpl extends ServiceImpl<FamilyLawRecordsMapper, FamilyLawRecords> implements FamilyLawRecordsService {

    @Autowired
    private FamilyLawRecordsMapper familyLawRecordsMapper;

    @Override
    public void editUserFamilyLaw(SysUser user, FamilyLawRecords familyLawRecords) {
        String userId = user.getId();
        familyLawRecordsMapper.deleteByUserId(userId);
        familyLawRecordsMapper.insert(familyLawRecords);
    }
}
