package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.DocRecord;
import com.zs.create.modules.system.entity.Done;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndexCountMapper extends BaseMapper<Done> {
    List<Done> queryDone(@Param("id") String id);

    List<Done> query(@Param("id") String id);
}
