package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.ZsMeettingRecord;
import com.zs.create.modules.system.mapper.ZsMeettingRecordMapper;
import com.zs.create.modules.system.service.ZsMeettingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 会议记录
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-17
 */
@Service
@Slf4j
public class ZsMeettingRecordServiceImpl extends ServiceImpl<ZsMeettingRecordMapper, ZsMeettingRecord> implements ZsMeettingRecordService {

    @Autowired
    private ZsMeettingRecordMapper zsMeettingRecordMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ZsMeettingRecord zsMeettingRecord) throws Exception {
        try {
            zsMeettingRecordMapper.insert(zsMeettingRecord);
        } catch (Exception e) {
            throw new Exception("SaveZsMeettingRecordError");
        }
    }

    @Override
    public ZsMeettingRecord selectById(String id) {
        ZsMeettingRecord zsMeettingRecord = zsMeettingRecordMapper.select(id);
        return zsMeettingRecord;
    }
}
