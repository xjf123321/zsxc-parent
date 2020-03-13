package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.entity.ZsRemindRecord;
import com.zs.create.modules.system.entity.ZsSqNotice;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息提醒 service层接口
 * </p>
 * @Author yaochao
 * @since 2019-11-19
 */
public interface ZsRemindRecordService extends IService<ZsRemindRecord> {
    void add(ZsDocRecord zsDocRecord);

    void addNotice(ZsSqNotice zsSqNotice);

    List<ZsRemindRecord> select(Date txsj);
}
