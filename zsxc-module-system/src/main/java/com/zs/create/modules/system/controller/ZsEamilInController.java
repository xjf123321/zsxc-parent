package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsEmailRecording;
import com.zs.create.modules.system.service.ZsEmailRecordingService;
import com.zs.create.modules.system.service.ZsEmailService;
import com.zs.create.modules.system.vo.ZsEmailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * zs_email表 前端控制器
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@RestController
@Api(tags = "收件箱")
@RequestMapping("/inbox")
@Slf4j
public class ZsEamilInController {
    @Autowired
    private ZsEmailService zsEmailService;

    @Autowired
    private ZsEmailRecordingService zsEmailRecordingService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "收件箱-分页列表查询")
    @ApiOperation(value = "收件箱-分页列表查询", notes = "收件箱-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ZsEmailVO>> queryPageList(ZsEmailVO zsEmailVO,
                                                      @RequestParam(name = "keyword", required = false) String keyword,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        Result<IPage<ZsEmailVO>> result = new Result<>();
        Page<ZsEmailVO> page = new Page<ZsEmailVO>(pageNo, pageSize);
        IPage<ZsEmailVO> pageList = zsEmailService.searchInbox(page, zsEmailVO, keyword);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "收件箱-通过id删除")
    @ApiOperation(value = "收件箱-通过id删除", notes = "收件箱-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            ZsEmailRecording recording = new ZsEmailRecording();
            recording.setEmailId(id);
            recording.setDelFlag(1);
            zsEmailRecordingService.updateStatus(recording);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    @AutoLog(value = "收件箱-批量删除")
    @ApiOperation(value = "收件箱-批量删除", notes = "收件箱-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ZsEmailVO> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsEmailVO> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            List<String> list = Arrays.asList(ids.split(","));
            for (int i = 0; i < list.size(); i++) {
                ZsEmailRecording recording = new ZsEmailRecording();
                recording.setEmailId(list.get(i));
                recording.setDelFlag(1);
                zsEmailRecordingService.updateStatus(recording);
            }
            result.success("删除成功!");
        }
        return result;
    }

    @AutoLog(value = "收件箱-通过id查询")
    @ApiOperation(value = "收件箱-通过id查询", notes = "收件箱-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ZsEmailVO> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ZsEmailVO> result = new Result<>();
        ZsEmailRecording recording = new ZsEmailRecording();
        recording.setEmailId(id);
        recording.setReadType(1);
        zsEmailRecordingService.updateStatus(recording);
        ZsEmailVO email = zsEmailService.findById(id);
        if (email == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(email);
            result.setSuccess(true);
        }
        return result;
    }
    /**
     * 添加
     */
    @AutoLog(value = "收件箱-写信")
    @ApiOperation(value = "收件箱-写信", notes = "收件箱-写信")
    @PostMapping(value = "/add")
    public Result<ZsEmailVO> add(@RequestBody ZsEmailVO zsEmailVO) {
        Result<ZsEmailVO> result = new Result<>();
        try {
            zsEmailService.addEmail(zsEmailVO);
            result.success("发送成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "收件箱-标记为已读")
    @ApiOperation(value = "收件箱-标记为已读", notes = "收件箱-标记为已读")
    @DeleteMapping(value = "/sign")
    public Result<?> sign(@RequestParam(name = "id", required = true) String id) {
        try {
            ZsEmailRecording recording = new ZsEmailRecording();
            recording.setEmailId(id);
            recording.setReadType(1);
            zsEmailRecordingService.updateStatus(recording);
        } catch (Exception e) {
            log.error("标记失败", e.getMessage());
            return Result.error("标记失败!");
        }
        return Result.ok("标记成功!");
    }

    @GetMapping("/reply")
    @AutoLog(value = "收件箱-收件箱回复展示")
    @ApiOperation(value = "收件箱-收件箱回复展示", notes = "收件箱-收件箱回复展示")
    public Result<ZsEmailVO> reply(@RequestParam(value = "id", required = true) String id) {
        Result<ZsEmailVO > result = new Result<>();
        ZsEmailVO emailVO = zsEmailService.reply(id);
        result.setSuccess(true);
        result.setResult(emailVO);
        return result;
    }

    @GetMapping("/forward")
    @AutoLog(value = "收件箱-转发")
    @ApiOperation(value = "收件箱-转发", notes = "收件箱-转发")
    public Result<ZsEmailVO> forward(@RequestParam(name = "id", required = true) String id) {
        Result<ZsEmailVO> result = new Result<>();
        ZsEmailVO zsEmailVO = zsEmailService.forward(id);
        result.setSuccess(true);
        result.setResult(zsEmailVO);
        return result;
    }


    @AutoLog(value = "收件箱-编辑页面存为草稿")
    @ApiOperation(value = "收件箱-编辑页面存为草稿", notes = "收件箱-编辑页面存为草稿")
    @PostMapping(value = "/addDraft")
    public Result<ZsEmailVO> addDraft(@RequestBody ZsEmailVO zsEmailVO) {
        Result<ZsEmailVO> result = new Result<>();
        try {
            zsEmailService.addDraft(zsEmailVO);
            result.success("添加草稿成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("添加草稿失败");
        }
        return result;
    }

}
