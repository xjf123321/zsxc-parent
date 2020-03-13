package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.common.exception.ZsxcBootException;
import com.zs.create.modules.system.entity.SysCategory;
import com.zs.create.modules.system.model.TreeSelectModel;

import java.util.List;

/**
 * @Description: 分类字典
 * @Author: zsxc
 * @Date: 2019-05-29
 * @Version: V1.0
 */
public interface ISysCategoryService extends IService<SysCategory> {

    /**
     * 根节点父ID的值
     */
    public static final String ROOT_PID_VALUE = "0";

    void addSysCategory(SysCategory sysCategory);

    void updateSysCategory(SysCategory sysCategory);

    /**
     * 根据父级编码加载分类字典的数据
     *
     * @param pcode
     * @return
     */
    public List<TreeSelectModel> queryListByCode(String pcode) throws ZsxcBootException;

    /**
     * 根据pid查询子节点集合
     *
     * @param pid
     * @return
     */
    public List<TreeSelectModel> queryListByPid(String pid);

}
