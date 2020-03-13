package com.zs.create.modules.process.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.service.DocRecordService;
import com.zs.create.modules.system.service.ZsDocRecordService;
import com.zs.create.modules.system.vo.ZsDocRecordVo;
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
@RequestMapping("/docApply")
@Api(tags = "待办事项公文")
@Slf4j
public class DocFlowController {

    @Autowired
    private ZsDocRecordService zsDocRecordService;

    @Autowired
    private DocRecordService docRecordService;

    /*
     * 公文分页查询
     * */
    @AutoLog(value = "待办事项公文列表")
    @ApiOperation(value = "待办事项公文列表", notes = "待办事项公文列表")
    @RequestMapping(value = "/queryLeaderZsDocRecordList", method = RequestMethod.GET)
    public Result<TPage<ZsDocRecord>> queryZsDocRecordList(ZsDocRecord zsDocRecord,
                                                           @RequestParam(name = "title", required = false) String title,
                                                           @RequestParam(name = "username", required = false) String username,
                                                           @RequestParam(name = "emergencyLevel", required = false) String emergencyLevel,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsDocRecord> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsDocRecordService.queryLeaderDocRecord(zsDocRecord, emergencyLevel, title, username, pageNo, pageSize);
        page.setRecords((List<ZsDocRecord>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /*
     * 根据id查询审批单信息(公文)
     * */
    @GetMapping(value = "/docFormShow")
    @ApiOperation("我的任务列表")
    public Result formShow(@RequestParam(name = "id", required = false) String id) {
        Result result = new Result<>();
        if (StringUtils.isEmpty(id)) {
            result.error500("任务ID参数不能为空!");
        } else {
            try {
                Map map = zsDocRecordService.formShow(id);
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
    @RequestMapping(value = "/docApproval", method = RequestMethod.PUT)
    public Result<ZsDocRecord> docApproval(@RequestBody ZsDocRecord zsDocRecord) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            docRecordService.docRecordAdd(zsDocRecord);
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
    @RequestMapping(value = "/docNotApproval", method = RequestMethod.PUT)
    public Result<ZsDocRecord> docNotApproval(@RequestBody ZsDocRecord zsDocRecord) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            docRecordService.notDocRecordAdd(zsDocRecord);
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
    public Result<ZsDocRecord> batchPresentation(@RequestBody ZsDocRecord zsDocRecord) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            docRecordService.addBatch(zsDocRecord);
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
    public Result<ZsDocRecord> release(@RequestParam(name = "id", required = false) String id,
                                       @RequestParam(name = "receiver", required = false) String receiver,
                                       @RequestParam(name = "receiverName", required = false) String receiverName) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            zsDocRecordService.release(id, receiver, receiverName);
            result.success("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 公文已办事项列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/haveDoneList")
    @ApiOperation("公文已办事项列表")
    public Result<IPage<ZsDocRecord>> haveDoneList(@RequestParam(name = "realname", required = false) String realname,
                                               @RequestParam(name = "username", required = false) String username,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ZsDocRecord>> result = new Result<>();
        Page<ZsDocRecord> page = new Page<>(pageNo, pageSize);
        IPage<ZsDocRecord> pageList = zsDocRecordService.haveDoneList(page, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /*
    * 呈批给保密员
    * */
    @ApiOperation("呈批给保密员")
    @RequestMapping(value = "/secrecy",method = RequestMethod.PUT)
    public Result<ZsDocRecord> secrecy(@RequestBody ZsDocRecord zsDocRecord){
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            zsDocRecordService.updateSendState(zsDocRecord.getId(),"2");
            //插入过程表
            docRecordService.addProcess(zsDocRecord);
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
    public Result<ZsDocRecord> noSecrecy(@RequestBody ZsDocRecord zsDocRecord){
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            zsDocRecordService.updateSendState(zsDocRecord.getId(),"0");
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
    public Result<ZsDocRecord> coApproval(@RequestBody ZsDocRecord zsDocRecord){
        Result<ZsDocRecord> result = new Result<>();
        try {
            zsDocRecordService.coApproval(zsDocRecord,null);
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
        zsDocRecordService.look(id);
    }


    /*
     * 已读和未读
     * */
    @GetMapping(value = "/read")
    @ApiOperation("我的任务列表")
    public Result<ZsDocRecordVo> read(@RequestParam(name = "id", required = false) String id) {
        Result result = new Result<>();
        ZsDocRecordVo zsDocRecordVo = zsDocRecordService.read(id);
        result.setResult(zsDocRecordVo);
        return result;
    }
}
