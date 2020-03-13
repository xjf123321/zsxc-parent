package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.FamilyLawRecords;

import java.util.List;

public interface FamilyLawRecordsMapper extends BaseMapper<FamilyLawRecords> {
    List<FamilyLawRecords> selectFLByUserId(String id);

    void deleteByUserId(String userId);
}
