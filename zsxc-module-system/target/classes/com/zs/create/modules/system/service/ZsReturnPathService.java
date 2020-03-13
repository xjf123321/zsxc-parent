package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.entity.ZsReturnPath;
import com.zs.create.modules.system.entity.ZsSqNotice;

public interface ZsReturnPathService extends IService<ZsReturnPath> {
    ZsDocRecord updateUrl(String id, String url);

    ZsSqNotice updateNotice(String id, String s);

}
