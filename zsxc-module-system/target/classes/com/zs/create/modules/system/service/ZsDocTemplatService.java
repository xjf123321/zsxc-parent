package com.zs.create.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsDocTemplate;

/**
 * <p>
 * 公文模板表service接口
 * </p>
 * @Author yaochao
 * @since 2019-10-22
 */
public interface ZsDocTemplatService extends IService<ZsDocTemplate> {
    void removeDoc(String id);

    void saveDoc(JSONObject jsonObject);

}
