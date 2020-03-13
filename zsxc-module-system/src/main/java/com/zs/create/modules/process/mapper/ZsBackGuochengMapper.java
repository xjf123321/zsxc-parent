package com.zs.create.modules.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.process.entity.ZsBackGuocheng;

import java.util.List;
/**
 * <p>
 * 销假过程表 Mapper 接口
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-8
 */
public interface ZsBackGuochengMapper extends BaseMapper<ZsBackGuocheng> {
    List<ZsBackGuocheng> formShow(String id);
}
