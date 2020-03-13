package com.zs.create.modules.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.service.ZsSqCarTwoService;
import com.zs.create.vo.TPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/carTwoApply")
@Api(tags = "待办事项车辆（2）")
public class VehicleFlowTwoController {

    @Autowired
    private ZsSqCarTwoService zsSqCarTwoService;

    @GetMapping(value = "/carApproval")
    @ApiOperation("待办事项车辆列表")
    public Result<IPage<ZsSqCar>> workList(@RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                           @RequestParam(name = "title", required = false) String title,
                                           @RequestParam(name = "username", required = false) String username,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsSqCar> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsSqCarTwoService.carApplyList(emergencyLevel, title, username, pageNo, pageSize);
        page.setRecords((List<ZsSqCar>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /*
     * 同意
     * */
    @ApiOperation("同意(领导审批同意)")
    @RequestMapping(value = "/agree", method = RequestMethod.PUT)
    public Result<ZsSqCar> agree(@RequestBody ZsSqCar zsSqCar) {
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        try {
            zsSqCarTwoService.agree(zsSqCar);
            result.success("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
    * 不同意
    * */
    @ApiOperation("不同意(领导审批不同意)")
    @RequestMapping(value = "/reject", method = RequestMethod.PUT)
    public Result<ZsSqCar> reject(@RequestBody ZsSqCar zsSqCar){
        Result<ZsSqCar> result = new Result<ZsSqCar>();
        try {
            zsSqCarTwoService.reject(zsSqCar);
            result.success("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
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
        IPage<ZsSqCar> pageList = zsSqCarTwoService.haveDoneList(page, username);
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
                Map map = zsSqCarTwoService.formShow(id);
                result.setSuccess(true);
                result.setResult(map);
            } catch (Exception e) {
                result.error500(e.getMessage());
            }
        }
        return result;
    }
}
