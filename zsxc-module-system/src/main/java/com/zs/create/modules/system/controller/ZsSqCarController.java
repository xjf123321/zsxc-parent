package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.service.ZsSqCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 车辆申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@RestController
@RequestMapping("/carApply")
@Api(tags = "车辆申请")
@Slf4j
public class ZsSqCarController {
    @Autowired
    private ZsSqCarService zsSqCarService;



    /*
     * 分页查询
     * */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "测试-分页列表查询")
    @RequestMapping(value = "/queryZsSqCarList", method = RequestMethod.GET)
    public Result<IPage<ZsSqCar>> queryConferenceRoomList(ZsSqCar zsSqCar, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<ZsSqCar>> result = new Result<>();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsSqCar.setDelFlag("0");
        zsSqCar.setUserId(sysUser.getId());
        QueryWrapper<ZsSqCar> queryWrapper = QueryGenerator.initQueryWrapper(zsSqCar, req.getParameterMap());
        Page<ZsSqCar> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqCar> pageList = zsSqCarService.page(page, queryWrapper);
        List<ZsSqCar> list = pageList.getRecords();
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /*
     * 新增
     * */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsSqCar> add(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        try {
            zsSqCarService.add(zsSqCar);
            result.success("添加成功");
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
    public Result<ZsSqCar> update(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.getById(zsSqCar.getId());
        if (zsSqCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqCarService.updateById(zsSqCar);
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
    public Result<ZsSqCar> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar = zsSqCarService.getById(id);
        if (zsSqCar == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsSqCar.setDelFlag("1");
                zsSqCarService.updateById(zsSqCar);
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
    public Result<ZsSqCar> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsSqCar zsSqCar = zsSqCarService.getById(arrs[i]);
                    zsSqCar.setDelFlag("1");
                    zsSqCarService.updateById(zsSqCar);
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
    * 根据申请id查询（个人代办、催办）
    * */
    @AutoLog(value = "根据id查询")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    public Result selectById(@RequestParam(name = "id", required = true) String id) {
        Result result = new Result<ZsSqCar>();
        try {
            Map map = zsSqCarService.select(id);
            result.setResult(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
