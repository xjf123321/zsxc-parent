package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.LawRecords;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.mapper.LawRecordsMapper;
import com.zs.create.modules.system.service.LawRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LawRecordsServiceImpl extends ServiceImpl<LawRecordsMapper, LawRecords> implements LawRecordsService {

    @Autowired
    private LawRecordsMapper lawRecordsMapper;

    @Override
    public void editUserLaw(SysUser user, LawRecords lawRecords) {
        String userId = user.getId();
        lawRecordsMapper.deleteByUserId(userId);
        lawRecordsMapper.insert(lawRecords);
    }
}
