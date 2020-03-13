package com.zs.create.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.process.entity.ZsLeaveGuocheng;
import com.zs.create.modules.process.service.ZsLeaveGuochengService;
import com.zs.create.modules.process.service.ZsSqLeaveTwoService;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.service.ISysUserService;
import com.zs.create.modules.system.service.ZsSqLeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 待办事项请假
 * </p>
 *
 * @Author yaochao
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/leaveTwo")
@Api(tags = "待办事项请假第二版本")
@Slf4j
public class LeaveFlowTwoController {


    @Autowired
    private ZsSqLeaveTwoService zsSqLeaveTwoService;

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
        IPage<ZsSqLeave> pageList = zsSqLeaveTwoService.leaveApplyList(page, emergencyLevel, title, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 同意
     * @param zsSqLeave
     * @return
     */
    @AutoLog(value = "同意")
    @ApiOperation(value = "同意", notes = "同意")
    @RequestMapping(value = "/approval", method = RequestMethod.PUT)
    public Result<ZsSqLeave> approval(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveTwoService.findById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqLeaveTwoService.updateState(zsSqLeave);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }


    /**
     * 驳回
     * @param zsSqLeave
     * @return
     */
    @AutoLog(value = "驳回")
    @ApiOperation(value = "驳回", notes = "驳回")
    @RequestMapping(value = "/disagree", method = RequestMethod.PUT)
    public Result<ZsSqLeave> disagree(@RequestBody ZsSqLeave zsSqLeave) {
        Result<ZsSqLeave> result = new Result<ZsSqLeave>();
        ZsSqLeave zsSqLeave1 = zsSqLeaveTwoService.findById(zsSqLeave.getId());
        if (zsSqLeave1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqLeaveTwoService.disagree(zsSqLeave);
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
                Map map = zsSqLeaveTwoService.formShow(id);
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
        IPage<ZsSqLeave> pageList = zsSqLeaveTwoService.haveDoneList(page, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 请假管理人事列表
     * @param realname
     * @param username
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/leaveList")
    @ApiOperation("请假管理人事列表")
    public Result<IPage<ZsSqLeave>> leaveList(ZsSqLeave zsSqLeave,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqLeave>> result = new Result<>();
        Page<ZsSqLeave> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqLeave> pageList = zsSqLeaveTwoService.leaveList(page, zsSqLeave);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(ZsSqLeave zsSqLeave,
                                  @RequestParam(name = "selections", required = false) String selections,
                                  HttpServletRequest request) {
        // Step.1 组装查询条件
        List<ZsSqLeave> zsSqLeaveList = null;
        if (selections != null && !"".equals(selections)) {
            List<String> list = Arrays.asList(selections.split(","));
            zsSqLeaveList = zsSqLeaveTwoService.findByIds(list);
        } else {
            zsSqLeaveList = zsSqLeaveTwoService.findAll(zsSqLeave);
        }
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());

        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "请假列表");
        mv.addObject(NormalExcelConstants.CLASS, ZsSqLeave.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("请假列表数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, zsSqLeaveList);
        return mv;
    }



}
