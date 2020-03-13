package com.zs.create.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import com.zs.create.modules.process.service.ZsLeaveGuochengService;
import com.zs.create.modules.process.service.ZsSqLeaveTwoService;
import com.zs.create.modules.system.entity.ZsSqLeave;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @Author yaochao
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/leaveApplyTwo")
@Api(tags = "请假申请第二版本")
@Slf4j
public class ZsSqLeaveTwoController {

    @Autowired
    private ZsSqLeaveTwoService zsSqLeaveTwoService;

    @Autowired
    private ZsLeaveGuochengService zsLeaveGuochengService;

    /*
     * 分页查询
     * */
    @AutoLog(value = "请假-分页列表查询")
    @ApiOperation(value = "请假-分页列表查询", notes = "请假-分页列表查询")
    @RequestMapping(value = "/queryZsSqRoomList", method = RequestMethod.GET)
    public Result<IPage<ZsSqLeave>> queryZsSqRoomList(ZsSqLeave zsSqLeave,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        Result<IPage<ZsSqLeave>> result = new Result<>();
        Page<ZsSqLeave> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqLeave> pageList = zsSqLeaveTwoService.findList(page, zsSqLeave);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }


    /*
     * 新增
     * */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsSqLeave> add(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        Date startDate = zsSqLeave.getStartDate();
        Date endDate = zsSqLeave.getEndDate();
        int date = startDate.compareTo(endDate);
        if (date == 1) {
            return result.error500("开始时间大于结束时间");
        }
        try {
            zsSqLeaveTwoService.add(zsSqLeave);
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;

    }

    /*
     * 删除
     * */
    @AutoLog(value = "根据id删除")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<ZsSqLeave> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave = zsSqLeaveTwoService.findById(id);
        if (zsSqLeave == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsSqLeave.setDelFlag("1");
                zsSqLeaveTwoService.updateById(zsSqLeave);
                QueryWrapper<ZsLeaveGuocheng> qw = new QueryWrapper<>();
                qw.eq("leave_id", id);
                zsLeaveGuochengService.remove(qw);
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
     * 编辑
     * */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<ZsSqLeave> edit(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        Date startDate = zsSqLeave.getStartDate();
        Date endDate = zsSqLeave.getEndDate();
        int date = startDate.compareTo(endDate);
        if (date == 1) {
            return result.error500("开始时间大于结束时间");
        }
        try {
            zsSqLeaveTwoService.edit(zsSqLeave);
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;

    }

}
