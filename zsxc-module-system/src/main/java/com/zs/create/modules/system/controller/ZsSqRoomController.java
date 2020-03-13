package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.process.entity.ZsMeettingGuocheng;
import com.zs.create.modules.system.entity.ZsSqRoom;
import com.zs.create.modules.process.service.ZsMeettingGuochengService;
import com.zs.create.modules.system.service.ZsMeettingRecordService;
import com.zs.create.modules.system.service.ZsSqRoomService;
import com.zs.create.modules.system.vo.ZsSqRoomVo;
import com.zs.create.vo.TPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会议室申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@RestController
@RequestMapping("/meettingApply")
@Api(tags = "会议室申请")
@Slf4j
public class ZsSqRoomController {

    @Autowired
    private ZsSqRoomService zsSqRoomService;

    @Autowired
    private ZsMeettingRecordService zsMeettingRecordService;


    @Autowired
    private ZsMeettingGuochengService zsMeettingProcessService;

    /*
    * 分页查询
    * */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @RequestMapping(value = "/queryZsSqRoomList", method = RequestMethod.GET)
    public Result<IPage<ZsSqRoom>> queryConferenceRoomList(ZsSqRoom zsSqRoom, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<ZsSqRoom>> result = new Result<>();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsSqRoom.setDelFlag("0");
        zsSqRoom.setUserId(sysUser.getId());
        QueryWrapper<ZsSqRoom> queryWrapper = QueryGenerator.initQueryWrapper(zsSqRoom, req.getParameterMap());
        Page<ZsSqRoom> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqRoom> pageList = zsSqRoomService.page(page, queryWrapper);
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
    public Result<ZsSqRoom> add(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<ZsSqRoom>();
        try {
            ZsMeettingGuocheng zsMeettingGuocheng = zsSqRoomService.add(zsSqRoom);
            result.success("添加成功");
            zsMeettingProcessService.add(zsMeettingGuocheng, zsSqRoom.getEmergencyLevel());
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
    public Result<ZsSqRoom> update(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<ZsSqRoom>();
        ZsSqRoom zsSqRoom1 = zsSqRoomService.getById(zsSqRoom.getId());
        if (zsSqRoom1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqRoomService.updateRoom(zsSqRoom);
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
    public Result<ZsSqRoom> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqRoom> result = new Result<ZsSqRoom>();
        ZsSqRoom zsSqRoom = zsSqRoomService.getById(id);
        if (zsSqRoom == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsSqRoom.setDelFlag("1");
                zsSqRoomService.updateById(zsSqRoom);
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
    public Result<ZsSqRoom> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsSqRoom> result = new Result<ZsSqRoom>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsSqRoom zsSqRoom = zsSqRoomService.getById(arrs[i]);
                    zsSqRoom.setDelFlag("1");
                    zsSqRoomService.updateById(zsSqRoom);
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
        Result result = new Result<>();
        try {
            Map map = zsSqRoomService.selectRoom(id);
            result.setResult(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 会议室记录
    * */
    @AutoLog(value = "会议记录管理")
    @ApiOperation(value = "会议记录管理", notes = "会议记录管理")
    @RequestMapping(value = "/meettingRecord", method = RequestMethod.GET)
    public Result<TPage<ZsSqRoomVo>> queryCollectList(ZsSqRoomVo zsSqRoomVo,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsSqRoomVo> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsSqRoomService.queryCollectList(zsSqRoomVo, pageNo, pageSize);
        page.setRecords((List<ZsSqRoomVo>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /*
     * 取消使用
     * */
    @AutoLog(value = "取消使用")
    @ApiOperation(value = "取消使用", notes = "取消使用")
    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public Result<ZsSqRoom> cancel(@RequestParam(name = "id", required = true) String id) {
        Result result = new Result();
        try {
            zsSqRoomService.cancel(id);
            result.success("取消成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }

        return result;
    }

    /*
    * 删除附件
    * */
    @AutoLog(value = "删除附件")
    @ApiOperation(value = "删除附件", notes = "删除附件")
    @RequestMapping(value = "/deleteAttachment", method = RequestMethod.DELETE)
    public Result<ZsSqRoom> deleteAttachment(@RequestParam String id){
        Result result = new Result();
        ZsSqRoom zsSqRoom = zsSqRoomService.getById(id);
        if (zsSqRoom == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsSqRoomService.deleteAttachment(id);
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;

    }
}
