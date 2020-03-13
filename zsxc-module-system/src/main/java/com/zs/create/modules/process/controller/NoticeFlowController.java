package com.zs.create.modules.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsSqNotice;
import com.zs.create.modules.system.service.NoticeProcessService;
import com.zs.create.modules.system.service.ZsSqNoticeService;
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
@RequestMapping("/noticwApply")
@Api(tags = "待办事项公告")
@Slf4j
public class NoticeFlowController {

    @Autowired
    private ZsSqNoticeService zsSqNoticeService;

    @Autowired
    private NoticeProcessService noticeProcessService;

    /*
     * 公文分页查询
     * */
    @AutoLog(value = "待办事项公告列表")
    @ApiOperation(value = "待办事项公告列表", notes = "待办事项公告列表")
    @RequestMapping(value = "/queryLeaderZsSqNoticeList", method = RequestMethod.GET)
    public Result<TPage<ZsSqNotice>> queryZsSqNoticeList(ZsSqNotice zsSqNotice,
                                                         @RequestParam(name = "title", required = false) String title,
                                                         @RequestParam(name = "username", required = false) String username,
                                                         @RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsSqNotice> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsSqNoticeService.queryLeaderNoticeProcess(zsSqNotice, emergencyLevel, title, username, pageNo, pageSize);
        page.setRecords((List<ZsSqNotice>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /*
     * 根据id查询审批单信息(公告)
     * */
    @GetMapping(value = "/noticeFormShow")
    @ApiOperation("我的任务列表")
    public Result formShow(@RequestParam(name = "id", required = false) String id) {
        Result result = new Result<>();
        if (StringUtils.isEmpty(id)) {
            result.error500("任务ID参数不能为空!");
        } else {
            try {
                Map map = zsSqNoticeService.formShow(id);
                result.setSuccess(true);
                result.setResult(map);
            } catch (Exception e) {
                result.error500(e.getMessage());
            }
        }
        return result;
    }

    /*
     * 同意
     * */
    @ApiOperation("同意(领导审批同意)")
    @RequestMapping(value = "/noticeApproval", method = RequestMethod.PUT)
    public Result<ZsSqNotice> noticeApproval(@RequestBody ZsSqNotice zsSqNotice) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            noticeProcessService.noticeProcessdAdd(zsSqNotice);
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
    @RequestMapping(value = "/noticeNotApproval", method = RequestMethod.PUT)
    public Result<ZsSqNotice> noticeNotApproval(@RequestBody ZsSqNotice zsSqNotice) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            noticeProcessService.notNoticeProcessdAdd(zsSqNotice);
            result.success("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 呈批
     * */
    @ApiOperation("呈批")
    @RequestMapping(value = "/batchPresentation", method = RequestMethod.PUT)
    public Result<ZsSqNotice> batchPresentation(@RequestBody ZsSqNotice zsSqNotice) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            noticeProcessService.addBatch(zsSqNotice);
            result.success("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 发布
     * */
    @ApiOperation("发布")
    @GetMapping(value = "/release")
    public Result<ZsSqNotice> release(@RequestParam(name = "id", required = false) String id,
                                      @RequestParam(name = "receiver", required = false) String receiver,
                                      @RequestParam(name = "receiverName", required = false) String receiverName) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            zsSqNoticeService.release(id, receiver, receiverName);
            result.success("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 公告已办事项列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/haveDoneList")
    @ApiOperation("公告已办事项列表")
    public Result<IPage<ZsSqNotice>> haveDoneList(@RequestParam(name = "realname", required = false) String realname,
                                                  @RequestParam(name = "username", required = false) String username,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsSqNotice>> result = new Result<>();
        Page<ZsSqNotice> page = new Page<>(pageNo, pageSize);
        IPage<ZsSqNotice> pageList = zsSqNoticeService.haveDoneList(page, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /*
     * 呈批给保密员
     * */
    @ApiOperation("呈批给保密员")
    @RequestMapping(value = "/secrecy",method = RequestMethod.PUT)
    public Result<ZsSqNotice> secrecy(@RequestBody ZsSqNotice zsSqNotice){
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            zsSqNoticeService.updateSendState(zsSqNotice.getId(),"2");
            //插入过程表
            noticeProcessService.addProcess(zsSqNotice);
            result.success("操作成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 不呈批给保密员
     * */
    @ApiOperation("不呈批给保密员")
    @RequestMapping(value = "/noSecrecy")
    public Result<ZsSqNotice> noSecrecy(@RequestBody ZsSqNotice zsSqNotice){
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            zsSqNoticeService.updateSendState(zsSqNotice.getId(),"0");
            result.success("操作成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 保密员同意
     * */
    @ApiOperation("保密员同意")
    @RequestMapping(value = "/coApproval", method = RequestMethod.PUT)
    public Result<ZsSqNotice> coApproval(@RequestBody ZsSqNotice zsSqNotice){
        Result<ZsSqNotice> result = new Result<>();
        try {
            zsSqNoticeService.coApproval(zsSqNotice,null);
            result.success("操作成功");
        } catch (Exception e){
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 已读
     * */
    @ApiOperation("已读")
    @GetMapping(value = "/look")
    public void look(@RequestParam String id){
        zsSqNoticeService.look(id);
    }
}
