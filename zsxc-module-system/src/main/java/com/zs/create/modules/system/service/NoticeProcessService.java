package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.NoticeProcess;
import com.zs.create.modules.system.entity.ZsSqNotice;

public interface NoticeProcessService extends IService<NoticeProcess> {
    void add(ZsSqNotice zsSqNotice);

    void noticeProcessdAdd(ZsSqNotice zsSqNotice);

    void notNoticeProcessdAdd(ZsSqNotice zsSqNotice);

    void addBatch(ZsSqNotice zsSqNotice);

    void addProcess(ZsSqNotice zsSqNotice);
}
