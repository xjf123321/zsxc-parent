package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsEmail;
import com.zs.create.modules.system.service.ZsEmailService;
import com.zs.create.modules.system.vo.ZsEmailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * zs_email表 前端控制器
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@RestController
@Api(tags = "发件箱")
@RequestMapping("/outbox")
@Slf4j
public class ZsEmailOutController {



    @Autowired
    private ZsEmailService zsEmailService;

/**
 * 分页列表查询
 */
    @AutoLog(value = "发件箱-分页列表查询")
    @ApiOperation(value = "发件箱-分页列表查询", notes = "发件箱-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ZsEmailVO>> queryPageList(ZsEmailVO zsEmailVO,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        Result<IPage<ZsEmailVO>> result = new Result<>();
        Page<ZsEmailVO> page = new Page<ZsEmailVO>(pageNo, pageSize);
        IPage<ZsEmailVO> pageList = zsEmailService.searchOutbox(page, zsEmailVO, keyword);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "发件箱-通过id删除")
    @ApiOperation(value = "发件箱-通过id删除", notes = "发件箱-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
           zsEmailService.updateDel(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    @AutoLog(value = "发件箱-批量删除")
    @ApiOperation(value = "发件箱-批量删除", notes = "发件箱-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ZsEmailVO> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsEmailVO> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
             zsEmailService.updateByIds(ids);
            }
            result.success("删除成功!");
        return result;
    }


    @AutoLog(value = "发件箱-通过id查询")
    @ApiOperation(value = "发件箱-通过id查询", notes = "发件箱-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ZsEmailVO> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ZsEmailVO> result = new Result<>();
        ZsEmailVO record = zsEmailService.findById(id);
        if (record == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(record);
            result.setSuccess(true);
        }
        return result;
    }

    @AutoLog(value = "发件箱-存为草稿")
    @ApiOperation(value = "发件箱-存为草稿", notes = "发件箱-存为草稿")
    @GetMapping(value = "/addDraft")
    public Result<?> addDraft(@RequestParam(name = "id", required = true) String id) {
        try {
            ZsEmail zsEmail = new ZsEmail();
            zsEmail.setId(id);
            zsEmail.setStatus(0);
           zsEmailService.updateStatus(zsEmail);
        } catch (Exception e) {
            log.error("保存草稿失败", e.getMessage());
            return Result.error("保存草稿失败!");
        }
        return Result.ok("保存草稿成功!");
    }



}
