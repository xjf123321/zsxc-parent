package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.service.ZsSqLeaveService;
import com.zs.create.vo.TPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@RestController
@RequestMapping("/leaveApply")
@Api(tags = "请假申请")
@Slf4j
public class ZsSqLeaveController {

    @Autowired
    private ZsSqLeaveService zsSqLeaveService;


    /*
     * 分页查询
     * */
    @AutoLog(value = "测试-分页列表查询")
    @ApiOperation(value = "测试-分页列表查询", notes = "测试-分页列表查询")
    @RequestMapping(value = "/queryZsSqRoomList", method = RequestMethod.GET)
    public com.zs.create.common.api.vo.Result<IPage<ZsSqLeave>> queryConferenceRoomList(ZsSqLeave zsSqLeave, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        TPage<ZsSqLeave> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String,Object> resultMap = zsSqLeaveService.queryZsSqLeave(zsSqLeave, pageNo, pageSize);
        page.setRecords((List<ZsSqLeave>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
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
            zsSqLeaveService.add(zsSqLeave);
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 编辑
     * */
    @AutoLog(value = "修改")
    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<ZsSqLeave> update(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveService.getById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqLeaveService.updateById(zsSqLeave1);
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
    public Result<ZsSqLeave> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave = zsSqLeaveService.getById(id);
        if (zsSqLeave == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsSqLeave.setDelFlag("1");
                zsSqLeaveService.updateById(zsSqLeave);
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
    public Result<ZsSqLeave> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsSqLeave zsSqLeave = zsSqLeaveService.getById(arrs[i]);
                    zsSqLeave.setDelFlag("1");
                    zsSqLeaveService.updateById(zsSqLeave);
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
    public Result<ZsSqLeave> selectById(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        try {
            ZsSqLeave zsSqLeave = zsSqLeaveService.selectById(id);
            result.setResult(zsSqLeave);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
