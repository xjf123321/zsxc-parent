package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ZsRemindRecord;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
/**
 * <p>
 * 消息通知 mapper接口
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */


public interface ZsRemindRecordMapper extends BaseMapper<ZsRemindRecord> {
    List<ZsRemindRecord> select(@Param("txsj") Date txsj, @Param("txdx") String txdx);
}
