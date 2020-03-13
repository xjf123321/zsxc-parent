package com.zs.create.modules.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * <p>
 * 待办事项车辆
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@RestController
@RequestMapping("/carApply")
@Api(tags = "待办事项车辆")
@Slf4j
public class VehicleFlowController {
    @Autowired
    private ZsSqCarService zsSqCarService;


    @Autowired
    private ZsCarGuochengService zsCarGuochengService;

    @Autowired
    private ZsBasicCarService zsBasicCarService;

    /**
     * 待办事项车辆列表
     *
     * @param emergencyLevel
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/carApproval")
    @ApiOperation("待办事项车辆列表")
    public Result<IPage<ZsSqCar>> workList(@RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                           @RequestParam(name = "title", required = false) String title,
                                           @RequestParam(name = "username", required = false) String username,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqCar>> result = new Result<>();
        Page<ZsSqCar> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqCar> pageList = zsSqCarService.carApplyList(emergencyLevel, page, title, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 领导同意
     *
     * @param zsSqCar
     * @return
     */
    @AutoLog(value = "同意(领导同意)")
    @ApiOperation(value = "同意(领导同意)", notes = "同意(领导同意)")
    @RequestMapping(value = "/approval", method = RequestMethod.PUT)
    public Result<ZsSqCar> approval(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.getById(zsSqCar.getId());
        if (zsSqCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqCar.setStatus("2");     //车辆管理员待派出
                zsSqCarService.updateStatus(zsSqCar);
                zsSqCar.setApprovalColumn("领导审批通过");
                zsCarGuochengService.add(zsSqCar);
                //车状态改成4
                String[] arr = zsSqCarService.selectPlateNumber(zsSqCar);
                zsBasicCarService.updateStatus(arr, "4");
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 呈批分部门领导
     *
     * @param zsSqCar
     * @return
     */
    @AutoLog(value = "同意且呈批(呈批分部门领导)")
    @ApiOperation(value = "同意且呈批(呈批分部门领导)", notes = "同意且呈批(呈批分部门领导)")
    @RequestMapping(value = "/groupApproval", method = RequestMethod.PUT)
    public Result<ZsSqCar> groupApproval(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.getById(zsSqCar.getId());
        if (zsSqCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqCar.setStatus("1");     //上级领导待审批
                zsSqCarService.updateStatus(zsSqCar);
                zsSqCar.setApprovalColumn("领导审批通过且呈批上级领导");
                zsCarGuochengService.addAndTx(zsSqCar);
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
     * @param zsSqCar
     * @return
     */
    @AutoLog(value = "不同意(领导不同意)")
    @ApiOperation(value = "不同意(领导不同意)", notes = "不同意(领导不同意)")
    @RequestMapping(value = "/disagree", method = RequestMethod.PUT)
    public Result<ZsSqCar> disagree(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.getById(zsSqCar.getId());
        if (zsSqCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqCar.setStatus("4");     //领导不同意
                zsSqCarService.updateStatus(zsSqCar);
                zsBasicCarService.updateState(zsSqCar);
                zsSqCar.setApprovalColumn("领导审批不通过");
                zsCarGuochengService.add(zsSqCar);
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
     * @param zsSqCar
     * @return
     */
    @AutoLog(value = "同意(分部门领导同意)")
    @ApiOperation(value = "同意(分部门领导同意)", notes = "同意(分部门领导同意)")
    @RequestMapping(value = "/leadership", method = RequestMethod.PUT)
    public Result<ZsSqCar> leadership(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.getById(zsSqCar.getId());
        if (zsSqCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqCar.setStatus("2");     //车辆管理员待派出
                zsSqCarService.updateStatus(zsSqCar);
                zsSqCar.setApprovalColumn("上级领导审批通过");
                zsCarGuochengService.add(zsSqCar);
                //车状态改成4
                String[] arr = zsSqCarService.selectPlateNumber(zsSqCar);
                zsBasicCarService.updateStatus(arr, "4");
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
     * @param zsSqCar
     * @return
     */
    @AutoLog(value = "不同意(分部门领导不同意)")
    @ApiOperation(value = "不同意(分部门领导不同意)", notes = "不同意(分部门领导不同意)")
    @RequestMapping(value = "/fbmdisagree", method = RequestMethod.PUT)
    public Result<ZsSqCar> fbmdisagree(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.getById(zsSqCar.getId());
        if (zsSqCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqCar.setStatus("5");     //分部门领导不同意
                zsSqCarService.updateStatus(zsSqCar);
                zsBasicCarService.updateState(zsSqCar);
                zsSqCar.setApprovalColumn("上级领导审批不通过");
                zsCarGuochengService.add(zsSqCar);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 车辆管理审核列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/vehicleOfficerList")
    @ApiOperation("车辆管理审核列表")
    public Result<IPage<ZsSqCar>> vehicleOfficerList(@RequestParam(name = "realname", required = false) String realname,
                                                     @RequestParam(name = "applyer", required = false) String applyer,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqCar>> result = new Result<>();
        Page<ZsSqCar> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqCar> pageList = zsSqCarService.vehicleOfficerList(page, applyer);
        result.setResult(pageList);
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
                Map map = zsSqCarService.formShow(id);
                result.setSuccess(true);
                result.setResult(map);
            } catch (Exception e) {
                result.error500(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 车辆已办事项列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/haveDoneList")
    @ApiOperation("车辆已办事项列表")
    public Result<IPage<ZsSqCar>> haveDoneList(@RequestParam(name = "realname", required = false) String realname,
                                               @RequestParam(name = "username", required = false) String username,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqCar>> result = new Result<>();
        Page<ZsSqCar> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqCar> pageList = zsSqCarService.haveDoneList(page, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 车辆管理员 派出
     *
     * @param zsSqCar
     * @return
     */
    @AutoLog(value = "派出(车辆管理员)")
    @ApiOperation(value = "派出(车辆管理员)", notes = "派出(车辆管理员)")
    @RequestMapping(value = "/vehicleOfficer", method = RequestMethod.PUT)
    public Result<ZsSqCar> vehicleOfficer(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        ZsSqCar zsSqCar1 = zsSqCarService.selectCar(zsSqCar.getId());
        try {
            zsSqCar.setStatus("3");     //待入库
            zsSqCarService.updateStatus(zsSqCar);
            zsSqCar.setApprovalColumn("车辆管理员派出车辆");
            zsBasicCarService.updateCarStatus(zsSqCar1);
            //zsCarGuochengService.add(zsSqCar);
            result.success("派车成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "入库(车辆管理员)")
    @ApiOperation(value = "入库(车辆管理员)", notes = "入库(车辆管理员)")
    @RequestMapping(value = "/updateCarStatusAndInDate", method = RequestMethod.GET)
    public Result<ZsSqCar> updateCarStatusAndInDate(@RequestParam(value = "id", required = true) String id) {
        Result<ZsSqCar> result = new Result();
        try {
            zsSqCarService.updateCarStatusAndInDate(id);
            ZsSqCar zsSqCar =  new ZsSqCar();
            zsSqCar.setId(id);
            zsSqCar.setStatus("4");     //结束
            zsSqCar.setApprovalColumn("车辆管理员入库车辆");
            //zsCarGuochengService.add(zsSqCar);
            result.success("入库成功");
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
        String flag = zsSqCarService.role();
        result.setResult(flag);
        return result;
    }
}
