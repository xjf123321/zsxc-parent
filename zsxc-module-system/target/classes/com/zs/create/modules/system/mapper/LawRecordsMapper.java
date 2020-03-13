package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.LawRecords;

import java.util.List;

public interface LawRecordsMapper extends BaseMapper<LawRecords> {

    List<LawRecords> selectLByUserId(String id);

    void updateLawRecords(String userId);

    void deleteByUserId(String userId);
}
