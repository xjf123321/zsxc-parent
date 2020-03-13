package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsEmailRecording;
/**
 * <p>
 * 记录表service层接口
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
public interface ZsEmailRecordingService extends IService<ZsEmailRecording> {
    void updateStatus(ZsEmailRecording recording);

    void delte(String id);

}
