package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsMeettingRecord;



/**
 * <p>
 * 会议记录
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-17
 */
public interface ZsMeettingRecordMapper extends BaseMapper<ZsMeettingRecord> {
    ZsMeettingRecord select(String id);
}
