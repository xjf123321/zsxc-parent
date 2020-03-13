package com.zs.create.modules.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.process.service.ZsLeaveGuochengService;
import com.zs.create.modules.system.entity.SysDepart;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.service.ZsSqLeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 待办事项请假
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@RestController
@RequestMapping("/leave")
@Api(tags = "待办事项请假")
@Slf4j
public class LeaveFlowController {

    @Autowired
    private ZsSqLeaveService zsSqLeaveService;

    @Autowired
    private ZsLeaveGuochengService zsLeaveGuochengService;

    /**
     * 待办事项请假列表
     *
     * @param emergencyLevel
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/leaveApproval")
    @ApiOperation("待办事项请假列表")
    public Result<IPage<ZsSqLeave>> leaveApproval(@RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                                  @RequestParam(name = "username", required = false) String username,
                                                  @RequestParam(name = "title", required = false) String title,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqLeave>> result = new Result<>();
        Page<ZsSqLeave> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqLeave> pageList = zsSqLeaveService.leaveApplyList(page, emergencyLevel, title, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 领导同意
     * @param zsSqLeave
     * @return
     */
    @AutoLog(value = "同意(领导同意)")
    @ApiOperation(value = "同意(领导同意)", notes = "同意(领导同意)")
    @RequestMapping(value = "/approval", method = RequestMethod.PUT)
    public Result<ZsSqLeave> approval(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveService.getById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                ZsSqLeave zsSqLeave2 = new ZsSqLeave();
                zsSqLeave2  = zsSqLeaveService.selectById(zsSqLeave.getId());
                BigDecimal leaveDays = new BigDecimal(zsSqLeave2.getNumber());
                BigDecimal days = new BigDecimal(1);
                Boolean depaerList =  zsSqLeaveService.listDeparts();
                if (leaveDays.compareTo(days) > 0) {     //请假天数大于1天 需呈批分部门领导进行审批
                    if (!depaerList) {
                        zsSqLeave.setStatus("2");     //结束
                        zsSqLeaveService.updateStatus(zsSqLeave);
                        zsSqLeave.setApprovalColumn("上级领导同意");
                        zsLeaveGuochengService.add(zsSqLeave);
                        result.success("审批成功");
                    } else {
                        zsSqLeave.setStatus("1");     //分部门领导待审批
                        zsSqLeaveService.updateStatus(zsSqLeave);
                        zsSqLeave.setApprovalColumn("领导审批通过且呈批上级领导");
                        zsLeaveGuochengService.addAndTx(zsSqLeave);
                    }
                } else if (leaveDays.compareTo(days) < 0  || leaveDays.compareTo(days) == 0) {   //请假天数小于1天 审批通过
                    zsSqLeave.setStatus("2");     //审批通过
                    zsSqLeaveService.updateStatus(zsSqLeave);
                    zsSqLeave.setApprovalColumn("审批通过");
                    zsLeaveGuochengService.add(zsSqLeave);
                } else {
                    result.error500("操作失败");
                    return result;
                }
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
     * @param zsSqLeave
     * @return
     */
    @AutoLog(value = "不同意(领导不同意)")
    @ApiOperation(value = "不同意(领导不同意)", notes = "不同意(领导不同意)")
    @RequestMapping(value = "/disagree", method = RequestMethod.PUT)
    public Result<ZsSqLeave> disagree(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveService.getById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                Boolean depaerList = zsSqLeaveService.listDeparts();
                if (!depaerList) {
                    zsSqLeave.setStatus("4");     //分部门领导不同意
                    zsSqLeaveService.updateStatus(zsSqLeave);
                    zsSqLeave.setApprovalColumn("上级领导不同意");
                    zsLeaveGuochengService.add(zsSqLeave);
                    result.success("审批成功");
                } else {
                    zsSqLeave.setStatus("3");     //领导不同意
                    zsSqLeaveService.updateStatus(zsSqLeave);
                    zsSqLeave.setApprovalColumn("领导不同意");
                    zsLeaveGuochengService.add(zsSqLeave);
                    result.success("审批成功");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 分部门领导同意
     * @param zsSqLeave
     * @return
     */
    @AutoLog(value = "同意(分部门领导同意)")
    @ApiOperation(value = "同意(分部门领导同意)", notes = "同意(分部门领导同意)")
    @RequestMapping(value = "/leadership", method = RequestMethod.PUT)
    public Result<ZsSqLeave> leadership(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveService.getById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqLeave.setStatus("2");     //结束
                zsSqLeaveService.updateStatus(zsSqLeave);
                zsSqLeave.setApprovalColumn("上级领导同意");
                zsLeaveGuochengService.add(zsSqLeave);
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
     * @param zsSqLeave
     * @return
     */
    @AutoLog(value = "不同意(分部门领导不同意)")
    @ApiOperation(value = "不同意(分部门领导不同意)", notes = "不同意(分部门领导不同意)")
    @RequestMapping(value = "/fbmdisagree", method = RequestMethod.PUT)
    public Result<ZsSqLeave> fbmdisagree(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveService.getById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqLeave.setStatus("4");     //分部门领导不同意
                zsSqLeaveService.updateStatus(zsSqLeave);
                zsSqLeave.setApprovalColumn("上级领导不同意");
                zsLeaveGuochengService.add(zsSqLeave);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 根据id查询表单和过程
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
                Map map = zsSqLeaveService.formShow(id);
                result.setSuccess(true);
                result.setResult(map);
            } catch (Exception e) {
                result.error500(e.getMessage());
            }
        }
        return result;
    }


    /**
     * 请假已办事项列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/haveDoneList")
    @ApiOperation("请假已办事项列表")
    public Result<IPage<ZsSqLeave>> haveDoneList(@RequestParam(name = "realname", required = false) String realname,
                                                 @RequestParam(name = "username", required = false) String username,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqLeave>> result = new Result<>();
        Page<ZsSqLeave> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqLeave> pageList = zsSqLeaveService.haveDoneList(page, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }






}
