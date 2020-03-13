package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.SysDataLog;

/**
 * @Description ISysDataLogService
 * @Author HeLiu
 * @Date 2019/9/28 9:59
 **/
public interface ISysDataLogService extends IService<SysDataLog> {

    /**
     * 添加数据日志
     *
     * @param tableName
     * @param dataId
     * @param dataContent
     */
    public void addDataLog(String tableName, String dataId, String dataContent);

}
