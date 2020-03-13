package com.zs.create.modules.system.controller;

import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.service.DocRecordService;
import com.zs.create.modules.system.service.ZsDocRecordService;
import com.zs.create.modules.system.service.ZsRemindRecordService;
import com.zs.create.vo.TPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公文
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-25
 */
@RestController
@RequestMapping("/zsdocrecord")
@Api(tags = "公文申请和查阅")
@Slf4j
public class ZsDocRecordController {

    @Autowired
    private ZsDocRecordService zsDocRecordService;


    @Autowired
    private DocRecordService docRecordService;

    @Autowired
    private ZsRemindRecordService zsRemindRecordService;


    /*
     * 分页查询
     * */
    @AutoLog(value = "公文申请分页列表查询")
    @ApiOperation(value = "公文申请分页列表查询", notes = "公文申请分页列表查询")
    @RequestMapping(value = "/queryZsDocRecordList", method = RequestMethod.GET)
    public Result<TPage<ZsDocRecord>> queryApplyZsDocRecordList(ZsDocRecord zsDocRecord,
                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsDocRecord> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsDocRecordService.queryZsDocRecord(zsDocRecord, pageNo, pageSize);
        page.setRecords((List<ZsDocRecord>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /*
    * 新增
    * */
    @AutoLog(value = "公文申请添加")
    @ApiOperation(value = "公文申请添加", notes = "公文申请添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsDocRecord> add(@RequestBody ZsDocRecord zsDocRecord) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        try {
            zsDocRecordService.add(zsDocRecord);
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
    * 删除
    * */
    @AutoLog(value = "根据id删除")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<ZsDocRecord> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
            try {
                zsDocRecordService.updateDelFlag(id);
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        return result;
    }

    /*
     * 批量删除
     * */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<ZsDocRecord> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsDocRecord> result = new Result<ZsDocRecord>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsDocRecord zsDocRecord = zsDocRecordService.getById(arrs[i]);
                    zsDocRecord.setDelFlag("1");
                    zsDocRecordService.updateById(zsDocRecord);
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
        Result result = new Result<ZsDocRecord>();
        try {
            Map map = zsDocRecordService.selectAllById(id);
            result.setResult(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 公文收文
    * */
    @AutoLog(value = "公文收文分页列表查询")
    @ApiOperation(value = "公文收文分页列表查询", notes = "公文收文分页列表查询")
    @RequestMapping(value = "/queryCollectList", method = RequestMethod.GET)
    public Result<TPage<ZsDocRecord>> queryCollectList(@RequestParam(name = "number", required = false) String number,
                                                       @RequestParam(name = "createTime", required = false) String createTime,
                                                                @RequestParam(name = "title", required = false) String title,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsDocRecord> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsDocRecordService.queryCollectList(number, createTime,title, pageNo, pageSize);
        page.setRecords((List<ZsDocRecord>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    @AutoLog(value = "公文查阅列表查询(pad)")
    @ApiOperation(value = "公文查阅列表查询(pad)", notes = "公文查阅列表查询(pad)")
    @RequestMapping(value = "/padCollectList", method = RequestMethod.GET)
    public Result<List<ZsDocRecord>> padCollectList() {
        Result<List<ZsDocRecord>> result = new Result<>();
        List<ZsDocRecord> zsDocRecordList = zsDocRecordService.padCollectList();
        result.setResult(zsDocRecordList);
        return result;
    }
}
