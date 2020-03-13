package com.zs.create.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsNoticeTemplate;
/**
 * <p>
 * 通知模板表表service接口
 * </p>
 * @Author yaochao
 * @since 2019-10-19
 */
public interface ZsNoticeTemplatService extends IService<ZsNoticeTemplate> {
    void saveNotice(JSONObject jsonObject);

    void removeNotice(String id);
}
