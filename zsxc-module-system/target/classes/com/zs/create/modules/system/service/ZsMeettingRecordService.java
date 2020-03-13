package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsMeettingRecord;


/**
 * <p>
 * 会议记录
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-17
 */
public interface ZsMeettingRecordService extends IService<ZsMeettingRecord> {
    void add(ZsMeettingRecord zsMeettingRecord) throws Exception;

    ZsMeettingRecord selectById(String id);
}
