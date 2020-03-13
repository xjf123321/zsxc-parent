package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.common.exception.ZsxcBootException;
import com.zs.create.modules.system.entity.SysPermission;
import com.zs.create.modules.system.model.TreeModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {

    public List<TreeModel> queryListByParentId(String parentId);

    /**
     * 真实删除
     */
    public void deletePermission(String id) throws ZsxcBootException;

    /**
     * 逻辑删除
     */
    public void deletePermissionLogical(String id) throws ZsxcBootException;

    public void addPermission(SysPermission sysPermission) throws ZsxcBootException;

    public void editPermission(SysPermission sysPermission) throws ZsxcBootException;

    public List<SysPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    public void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     *
     * @return
     */
    public List<String> queryPermissionUrlWithStar();
}
