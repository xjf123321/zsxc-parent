package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsSqBack;
import com.zs.create.modules.system.service.ZsSqBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 销假
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-18
 * */
@RestController
@RequestMapping("/backLeave")
@Api(tags = "销假管理")
@Slf4j
public class ZsSqBackController {
    @Autowired
    private ZsSqBackService zsSqBackService;


    /*
    * 分页查询
    * */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @RequestMapping(value = "/queryZsSqBackList", method = RequestMethod.GET)
    public Result<IPage<ZsSqBack>> queryConferenceRoomList(ZsSqBack zsSqBack, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<ZsSqBack>> result = new Result<>();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsSqBack.setDelFlag("0");
        zsSqBack.setUserId(sysUser.getId());
        QueryWrapper<ZsSqBack> queryWrapper = QueryGenerator.initQueryWrapper(zsSqBack, req.getParameterMap());
        Page<ZsSqBack> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqBack> pageList = zsSqBackService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /*
    * 添加
    * */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsSqBack> add(@RequestBody ZsSqBack zsSqBack) {
        Result<ZsSqBack> result = new Result<ZsSqBack>();
        try {
            zsSqBackService.add(zsSqBack);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
    * 修改
    * */
    @AutoLog(value = "修改")
    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<ZsSqBack> update(@RequestBody ZsSqBack zsSqBack) {
        Result<ZsSqBack> result = new Result<ZsSqBack>();
        ZsSqBack zsSqBack1 = zsSqBackService.getById(zsSqBack.getId());
        if (zsSqBack1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqBackService.updateById(zsSqBack);
                result.success("修改成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
    * 删除
    * */
    @AutoLog(value = "根据id删除")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<ZsSqBack> delete(@RequestParam String id) {
        Result<ZsSqBack> result = new Result<ZsSqBack>();
        ZsSqBack zsSqBack = zsSqBackService.getById(id);
        if (zsSqBack == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsSqBack.setDelFlag("1");
                zsSqBackService.updateById(zsSqBack);
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
    * 批量删除
    * */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<ZsSqBack> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsSqBack> result = new Result<ZsSqBack>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsSqBack zsSqBack = zsSqBackService.getById(arrs[i]);
                    zsSqBack.setDelFlag("1");
                    zsSqBackService.updateById(zsSqBack);
                }
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
    * 根据id查询申请表信息（个人代办、催办）
    * */
    @AutoLog(value = "根据id查询")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    public Result<ZsSqBack> selectById(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqBack> result = new Result<ZsSqBack>();
        try {
            ZsSqBack zsSqBack = zsSqBackService.selectById(id);
            result.setResult(zsSqBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
