package com.zs.create.modules.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;

import com.zs.create.modules.system.entity.ZsBasicRoom;
import com.zs.create.modules.system.entity.ZsSqRoom;
import com.zs.create.modules.process.service.ZsMeettingGuochengService;
import com.zs.create.modules.system.service.ZsBasicRoomService;
import com.zs.create.modules.system.service.ZsSqRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 待办事项会议
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-5
 */
@RestController
@RequestMapping("/meettingApply")
@Api(tags = "待办事项会议")
@Slf4j
public class ZsMeettingConrtoller {

    @Autowired
    private ZsSqRoomService zsSqRoomService;

    @Autowired
    private ZsMeettingGuochengService zsMeettingGuochengService;

    @Autowired
    private ZsBasicRoomService zsBasicRoomService;

    /**
     * 待办事项会议
     *
     * @param emergencyLevel
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/meettingApproval")
    @ApiOperation("待办事项会议列表")
    public Result<IPage<ZsSqRoom>> workList(@RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                            @RequestParam(name = "title", required = false) String title,
                                            @RequestParam(name = "username", required = false) String username,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqRoom>> result = new Result<>();
        Page<ZsSqRoom> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqRoom> pageList = zsSqRoomService.meettingApplyList(emergencyLevel, page, title, username);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     * 根据id查询表单和过程
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/formShow")
    @ApiOperation(value = "我的任务列表", notes = "根据id查询表单和过程")
    public Result formShow(@RequestParam(name = "id", required = false) String id) {
        Result result = new Result<>();
        if (StringUtils.isEmpty(id)) {
            result.error500("表单ID参数不能为空!");
        } else {
            try {
                Map map = zsSqRoomService.formShow(id);
                result.setSuccess(true);
                result.setResult(map);
            } catch (Exception e) {
                result.error500(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 领导同意
     *
     * @param zsSqRoom
     * @return
     */
    @AutoLog(value = "同意(领导同意)")
    @ApiOperation(value = "同意(领导同意)", notes = "同意(领导同意)")
    @RequestMapping(value = "/approval", method = RequestMethod.PUT)
    public Result<ZsSqRoom> approval(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<>();
        ZsSqRoom sqRoom = zsSqRoomService.getById(zsSqRoom.getId());
        if (sqRoom == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqRoom.setStatus("1");  //分管部门领导待审批
                zsSqRoomService.updateStatus(zsSqRoom, "2");
                zsSqRoom.setApprovalColumn("领导审批同意");
                zsMeettingGuochengService.addProcessAndTx(zsSqRoom, sqRoom.getEmergencyLevel());
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 领导不同意
     *
     * @param zsSqRoom
     * @return
     */
    @AutoLog(value = "不同意(领导不同意)")
    @ApiOperation(value = "不同意(领导不同意)", notes = "不同意(领导不同意)")
    @RequestMapping(value = "/disagree", method = RequestMethod.PUT)
    public Result<ZsSqRoom> disagree(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<>();
        ZsSqRoom sqRoom = zsSqRoomService.getById(zsSqRoom.getId());
        if (sqRoom == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqRoom.setStatus("4");  //领导不同意
                zsSqRoomService.updateStatus(zsSqRoom, "0");
                zsBasicRoomService.updateStatus(zsSqRoom);
                zsSqRoom.setApprovalColumn("领导不同意");
                zsMeettingGuochengService.addProcess(zsSqRoom);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }


    /**
     * 分部门领导同意
     *
     * @param zsSqRoom
     * @return
     */
    @AutoLog(value = "同意(会议管理员)")
    @ApiOperation(value = "同意(会议管理员)", notes = "同意(会议管理员)")
    @RequestMapping(value = "/leadership", method = RequestMethod.PUT)
    public Result<ZsSqRoom> leadership(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<>();
        ZsSqRoom sqRoom = zsSqRoomService.getById(zsSqRoom.getId());
        if (sqRoom == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqRoom.setStatus("2");  //审批通过
                zsSqRoomService.updateStatus(zsSqRoom, "3");
                zsSqRoom.setApprovalColumn("会议管理员审批通过");
                zsMeettingGuochengService.addProcess(zsSqRoom);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;

    }


    /**
     * 分部门领导不同意
     *
     * @param zsSqRoom
     * @return
     */
    @AutoLog(value = "不同意(会议管理员)")
    @ApiOperation(value = "不同意(会议管理员)", notes = "不同意(会议管理员)")
    @RequestMapping(value = "/fbmdisagree", method = RequestMethod.PUT)
    public Result<ZsSqRoom> fbmdisagree(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<>();
        ZsSqRoom sqRoom = zsSqRoomService.getById(zsSqRoom.getId());
        if (sqRoom == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqRoom.setStatus("5");  //分部门领导不同意
                zsSqRoomService.updateStatus(zsSqRoom, "0");
                zsSqRoom.setApprovalColumn("会议管理员审批不同意");
                zsMeettingGuochengService.addProcess(zsSqRoom);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }


    /**
     * 会议室管理审核列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/roomAdminList")
    @ApiOperation("会议室管理审核列表")
    public Result<IPage<ZsSqRoom>> roomAdminList(@RequestParam(name = "realname", required = false) String realname,
                                                 @RequestParam(name = "username", required = false) String username,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Result<IPage<ZsSqRoom>> result = new Result<>();
        Page<ZsSqRoom> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqRoom> pageList = zsSqRoomService.roomAdminList(page, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 已办事项会议
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */

    @GetMapping(value = "/meettingAllow")
    @ApiOperation("已办事项会议列表")
    public Result<IPage<ZsSqRoom>> meettingAllowList(@RequestParam(name = "realname", required = false) String realname,
                                                     @RequestParam(name = "username", required = false) String username,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqRoom>> result = new Result<>();
        Page<ZsSqRoom> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqRoom> pageList = zsSqRoomService.allowList(realname, page, username);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }


    /**
     * 会议室管理员操作
     *
     * @param zsSqRoom
     * @return
     */
    @AutoLog(value = "使用操作(会议管理员)")
    @ApiOperation(value = "使用操作(会议管理员)", notes = "使用操作(会议管理员)")
    @RequestMapping(value = "/updateMeettingStatus", method = RequestMethod.PUT)
    public Result<ZsSqRoom> updateMeettingStatus(@RequestBody ZsSqRoom zsSqRoom) {
        Result<ZsSqRoom> result = new Result<ZsSqRoom>();
        ZsSqRoom zsSqRoom1 = zsSqRoomService.getById(zsSqRoom.getId());
        if (zsSqRoom1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqRoom1.setStatus("6");     //待入库
                zsSqRoomService.updateZsSqStatus(zsSqRoom1);
                zsBasicRoomService.updateMeettingStatus(zsSqRoom1.getMeettingRoomName(),"1");
                result.success("操作成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    @AutoLog(value = "使用结束(会议管理员)")
    @ApiOperation(value = "使用结束(会议管理员)", notes = "使用结束(会议管理员)")
    @RequestMapping(value = "/updateMeettingStatusAndInDate", method = RequestMethod.GET)
    public Result<ZsSqRoom> updateMeettingStatusAndInDate(@RequestParam(value = "id", required = true) String id) {
        Result<ZsSqRoom> result = new Result();
        try {
            ZsSqRoom zsSqRoom = zsSqRoomService.getById(id);
            zsSqRoom.setStatus("7");
            zsSqRoomService.updateZsSqStatus(zsSqRoom);
            zsBasicRoomService.cleanTime(zsSqRoom.getMeettingRoomName());
            zsBasicRoomService.updateMeettingStatus(zsSqRoom.getMeettingRoomName(),"0");
            result.success("操作成功");
        } catch (Exception e) {
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "角色")
    @ApiOperation(value = "角色", notes = "角色")
    @RequestMapping(value = "/role", method = RequestMethod.PUT)
    public Result<String> role(){
        Result<String> result = new Result<>();
        String flag = zsSqRoomService.role();
        result.setResult(flag);
        return result;
    }
}




