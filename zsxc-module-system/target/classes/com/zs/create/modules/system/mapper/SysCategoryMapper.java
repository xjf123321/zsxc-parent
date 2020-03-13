package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.SysCategory;
import com.zs.create.modules.system.model.TreeSelectModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 分类字典
 * @Author: zsxc
 * @Date: 2019-05-29
 * @Version: V1.0
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

    /**
     * 根据父级ID查询树节点数据
     *
     * @param pid
     * @return
     */
    public List<TreeSelectModel> queryListByPid(@Param("pid") String pid);


}
