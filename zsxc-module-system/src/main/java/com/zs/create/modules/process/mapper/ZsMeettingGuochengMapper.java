package com.zs.create.modules.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.process.entity.ZsMeettingGuocheng;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会议审批记录
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-4
 */
public interface ZsMeettingGuochengMapper extends BaseMapper<ZsMeettingGuocheng> {

    List<ZsMeettingGuocheng> formShow(String id);

    List<String> selectRoomIds(@Param("userId") String userId);
}
