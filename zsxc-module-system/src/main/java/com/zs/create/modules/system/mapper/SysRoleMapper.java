package com.zs.create.modules.system.mapper;

import com.zs.create.modules.system.entity.SysRole;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-19
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> roleList(@Param("id") String id);

    String selectRoleId(@Param("roleCode") String roleCode);
}
