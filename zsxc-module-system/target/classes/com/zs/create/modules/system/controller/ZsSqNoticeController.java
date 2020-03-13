package com.zs.create.modules.system.controller;



import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsSqNotice;
import com.zs.create.modules.system.service.*;
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
 * 公告
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-25
 */
@RestController
@RequestMapping("/zsSqNotice")
@Api(tags = "通知公告管理")
@Slf4j
public class ZsSqNoticeController {

    @Autowired
    private ZsSqNoticeService zsSqNoticeService;


    @Autowired
    private NoticeProcessService noticeProcessService;

    @Autowired
    private ZsRemindRecordService zsRemindRecordService;


    /*
     * 分页查询
     * */
    @AutoLog(value = "通知公告发布分页列表查询")
    @ApiOperation(value = "通知公告发布分页列表查询", notes = "通知公告发布分页列表查询")
    @RequestMapping(value = "/queryZsSqNoticeList", method = RequestMethod.GET)
    public Result<TPage<ZsSqNotice>> queryApplyZsDocRecordList(ZsSqNotice zsSqNotice,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsSqNotice> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsSqNoticeService.queryZsSqNotice(zsSqNotice, pageNo, pageSize);
        page.setRecords((List<ZsSqNotice>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /*
     * 新增
     * */
    @AutoLog(value = "公告申请添加")
    @ApiOperation(value = "公告申请添加", notes = "公告申请添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsSqNotice> add(@RequestBody ZsSqNotice zsSqNotice) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        try {
            zsSqNoticeService.add(zsSqNotice);
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
    public Result<ZsSqNotice> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
            try {
                zsSqNoticeService.updateDelFlag(id);
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
    public Result<ZsSqNotice> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsSqNotice> result = new Result<ZsSqNotice>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsSqNotice zsSqNotice = zsSqNoticeService.getById(arrs[i]);
                    zsSqNotice.setDelFlag("1");
                    zsSqNoticeService.updateById(zsSqNotice);
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
        Result result = new Result<ZsSqNotice>();
        try {
            Map map = zsSqNoticeService.selectAllById(id);
            result.setResult(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 公告收文
     * */
    @AutoLog(value = "通知公告查阅分页列表查询")
    @ApiOperation(value = "通知公告查阅分页列表查询", notes = "通知公告查阅分页列表查询")
    @RequestMapping(value = "/queryCollectList", method = RequestMethod.GET)
    public Result<TPage<ZsSqNotice>> queryCollectList(@RequestParam(name = "number", required = false) String number,
                                                      @RequestParam(name = "title",required = false) String title,
                                                      @RequestParam(name = "createTime", required = false) String createTime,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<ZsSqNotice> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = zsSqNoticeService.queryCollectList(number, createTime,title, pageNo, pageSize);
        page.setRecords((List<ZsSqNotice>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }


    @AutoLog(value = "通知公告查阅列表查询(pad)")
    @ApiOperation(value = "通知公告查阅列表查询(pad)", notes = "通知公告查阅列表查询(pad)")
    @RequestMapping(value = "/padCollectList", method = RequestMethod.GET)
    public Result<List<ZsSqNotice>> padCollectList() {
       Result<List<ZsSqNotice>> result = new Result<>();
       List<ZsSqNotice> zsSqNoticeList = zsSqNoticeService.padCollectList();
       result.setResult(zsSqNoticeList);
       return result;
    }
}
