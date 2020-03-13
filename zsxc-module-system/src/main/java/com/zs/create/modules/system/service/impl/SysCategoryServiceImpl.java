package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.exception.ZsxcBootException;
import com.zs.create.common.util.oConvertUtils;
import com.zs.create.modules.system.entity.SysCategory;
import com.zs.create.modules.system.mapper.SysCategoryMapper;
import com.zs.create.modules.system.model.TreeSelectModel;
import com.zs.create.modules.system.service.ISysCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 分类字典
 * @Author: zsxc
 * @Date: 2019-05-29
 * @Version: V1.0
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

    @Override
    public void addSysCategory(SysCategory sysCategory) {
        if (oConvertUtils.isEmpty(sysCategory.getPid())) {
            sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
        } else {
            //如果当前节点父ID不为空 则设置父节点的hasChild 为1
            SysCategory parent = baseMapper.selectById(sysCategory.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.insert(sysCategory);
    }

    @Override
    public void updateSysCategory(SysCategory sysCategory) {
        if (oConvertUtils.isEmpty(sysCategory.getPid())) {
            sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
        } else {
            //如果当前节点父ID不为空 则设置父节点的hasChild 为1
            SysCategory parent = baseMapper.selectById(sysCategory.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.updateById(sysCategory);
    }

    @Override
    public List<TreeSelectModel> queryListByCode(String pcode) throws ZsxcBootException {
        String pid = ROOT_PID_VALUE;
        if (oConvertUtils.isNotEmpty(pcode)) {
            List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
            if (list == null || list.size() == 0) {
                throw new ZsxcBootException("该编码【" + pcode + "】不存在，请核实!");
            }
            if (list.size() > 1) {
                throw new ZsxcBootException("该编码【" + pcode + "】存在多个，请核实!");
            }
            pid = list.get(0).getId();
        }
        return baseMapper.queryListByPid(pid);
    }

    @Override
    public List<TreeSelectModel> queryListByPid(String pid) {
        if (oConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid);
    }

}
