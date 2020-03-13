package com.zs.create.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.message.entity.SysMessageTemplate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 消息模板
 * @Author: zsxc
 * @Date: 2019-04-09
 * @Version: V1.0
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate> {
    @Select("SELECT * FROM SYS_SMS_TEMPLATE WHERE TEMPLATE_CODE = #{code}")
    List<SysMessageTemplate> selectByCode(String code);
}
