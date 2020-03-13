package com.zs.create.modules.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;

import com.zs.create.modules.system.entity.ZsBasicRoom;
import com.zs.create.modules.system.mapper.ZsBasicRoomMapper;
import com.zs.create.modules.system.service.ZsBasicRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author xiajunfeng
 * @since 2019-10-12
 */
@RestController
@RequestMapping("/meetting")
@Api(tags = "会议室管理")
@Slf4j
public class ZsBasicRoomController {

    @Autowired
    private ZsBasicRoomService zsBasicRoomService;

    @Autowired
    private ZsBasicRoomMapper zsBasicRoomMapper;

    /*
    * 分页查询会议室信息
    * */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @RequestMapping(value = "/queryConferenceRoomList", method = RequestMethod.GET)
    public Result<IPage<ZsBasicRoom>> queryConferenceRoomList(ZsBasicRoom zsBasicRoom, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<ZsBasicRoom>> result = new Result<>();
        zsBasicRoom.setDelFlag("0");
        QueryWrapper<ZsBasicRoom> queryWrapper = QueryGenerator.initQueryWrapper(zsBasicRoom, req.getParameterMap());
        Page<ZsBasicRoom> page = new Page<>(pageNo, pageSize);
        IPage<ZsBasicRoom> pageList = zsBasicRoomService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /*
    * 添加会议室信息
    * */
    @AutoLog(value = "会议室添加")
    @ApiOperation(value = "会议室添加", notes = "会议室添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsBasicRoom> add(@RequestBody ZsBasicRoom zsBasicRoom) {
        Result<ZsBasicRoom> result = new Result<ZsBasicRoom>();
        long roomNumber = zsBasicRoomMapper.count(zsBasicRoom.getRoomNumber());
        if (roomNumber != 0) {
            result.error500("会议室编号重复");
            return result;
        }
        try {
            zsBasicRoomService.add(zsBasicRoom);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
    * 修改会议室信息
    * */
    @AutoLog(value = "修改")
    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<ZsBasicRoom> update(@RequestBody ZsBasicRoom zsBasicRoom) {
        Result<ZsBasicRoom> result = new Result<ZsBasicRoom>();
        ZsBasicRoom zsBasicRoom1 = zsBasicRoomService.getById(zsBasicRoom.getId());
        if (zsBasicRoom1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                ZsBasicRoom zsBasicRoom2 = zsBasicRoomService.updateDept(zsBasicRoom);
                zsBasicRoom2.setUpdateDate(new Date());
                zsBasicRoomService.updateById(zsBasicRoom2);
                result.success("修改成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
    * 删除会议室信息
    * */
    @AutoLog(value = "根据id删除")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<ZsBasicRoom> delete(@RequestParam String id) {
        Result<ZsBasicRoom> result = new Result<ZsBasicRoom>();
        ZsBasicRoom zsBasicRoom = zsBasicRoomService.getById(id);
        if (zsBasicRoom == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsBasicRoom.setDelFlag("1");
                zsBasicRoomService.updateById(zsBasicRoom);
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
    * 批量删除会议室信息
    * */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<ZsBasicRoom> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsBasicRoom> result = new Result<ZsBasicRoom>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsBasicRoom vehicle = zsBasicRoomService.getById(arrs[i]);
                    vehicle.setDelFlag("1");
                    zsBasicRoomService.updateById(vehicle);
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
     * 查询所有空闲状态会议室
     * */
    @AutoLog(value = "查询所有空闲状态会议室")
    @ApiOperation(value = "查询所有空闲状态会议室", notes = "查询所有空闲状态会议室")
    @RequestMapping(value = "/findAllByStatus", method = RequestMethod.GET)
    public Result<List<ZsBasicRoom>> findAllByStatus(@RequestParam(name = "startTime", required = false) String startTime,
                                                     @RequestParam(name = "endTime", required = false) String endTime) {
        Result<List<ZsBasicRoom>> result = new Result<>();
        List<ZsBasicRoom> list = zsBasicRoomService.selectRoom(startTime,endTime);
        if (list.size() == 0) {
            result.error500("暂时未有空闲会议室");
        }
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }


}
