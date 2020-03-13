package com.zs.create.modules.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.DocRecord;
import com.zs.create.modules.system.entity.ZsDocRecord;

public interface DocRecordService extends IService<DocRecord> {
    void add(ZsDocRecord zsDocRecord);

    void docRecordAdd(ZsDocRecord zsDocRecord);

    void notDocRecordAdd(ZsDocRecord zsDocRecord);

    void addBatch(ZsDocRecord zsDocRecord);

    void addProcess(ZsDocRecord zsDocRecord);
}
