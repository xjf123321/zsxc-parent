package com.zs.create.modules.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsSqBack;


import com.zs.create.modules.process.service.ZsBackGuochengService;
import com.zs.create.modules.system.service.ZsSqBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 待办事项销假
 *
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@RestController
@RequestMapping("/backFlow")
@Api(tags = "待办事项销假")
@Slf4j
public class BackFlowController {

    @Autowired
    private ZsSqBackService zsSqBackService;

    @Autowired
    private ZsBackGuochengService zsBackGuochengService;


    /**
     * 待办事项销假列表
     *
     * @param emergencyLevel
     * @param pageNo
     * @param pageSize
    * @return
     **/
    @GetMapping(value = "/backApproval")
    @ApiOperation("待办事项销假列表")
    public Result<IPage<ZsSqBack>> backApproval(@RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                                @RequestParam(name = "username", required = false) String username,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqBack>> result = new Result<>();
        Page<ZsSqBack> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqBack> pageList = zsSqBackService.backApplyList(emergencyLevel, page, username);
        result.setSuccess(true);
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
                Map map = zsSqBackService.formShow(id);
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
     * @param zsSqBack
     * @return
     */
    @AutoLog(value = "同意(领导同意)")
    @ApiOperation(value = "同意(领导同意)", notes = "同意(领导同意)")
    @RequestMapping(value = "/approval", method = RequestMethod.PUT)
    public Result<ZsSqBack> approval(@RequestBody ZsSqBack zsSqBack) {
        Result<ZsSqBack> result = new Result<>();
        ZsSqBack zsSqBack1 = zsSqBackService.getById(zsSqBack.getId());
        if (zsSqBack1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqBack.setStatus("1");    //审批通过（销假成功）
                zsSqBackService.updateStatus(zsSqBack);
                zsBackGuochengService.addRecord(zsSqBack);
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
     * @param zsSqBack
     * @return
     */
    @AutoLog(value = "不同意(领导不同意)")
    @ApiOperation(value = "不同意(领导不同意)", notes = "不同意(领导不同意)")
    @RequestMapping(value = "/disagree", method = RequestMethod.PUT)
    public Result<ZsSqBack> disagree(@RequestBody ZsSqBack zsSqBack) {
        Result<ZsSqBack> result = new Result<>();
        ZsSqBack zsSqBack1 = zsSqBackService.getById(zsSqBack.getId());
        if (zsSqBack1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsSqBack.setStatus("2");       //审批不通过（销假失败）
                zsSqBackService.updateStatus(zsSqBack);
                zsBackGuochengService.addRecord(zsSqBack);
                result.success("审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 人事列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/personnelList")
    @ApiOperation("人事列表")
    public Result<IPage<ZsSqBack>> personnelList(@RequestParam(name = "realname", required = false) String realname,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqBack>> result = new Result<>();
        Page<ZsSqBack> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqBack> pageList = zsSqBackService.personnelList(page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 已办事项销假
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */

    @GetMapping(value = "/backAllow")
    @ApiOperation("已办事项销假列表")
    public Result<IPage<ZsSqBack>> backAllowList(@RequestParam(name = "realname", required = false) String realname,
                                                 @RequestParam(name = "username", required = false) String username,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqBack>> result = new Result<>();
        Page<ZsSqBack> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqBack> pageList = zsSqBackService.allowList(realname, page, username);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }


}
