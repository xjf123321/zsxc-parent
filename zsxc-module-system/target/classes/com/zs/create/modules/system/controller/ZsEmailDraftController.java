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
@Api(tags = "草稿箱")
@RequestMapping("/draft")
@Slf4j
public class ZsEmailDraftController {
    @Autowired
    private ZsEmailService zsEmailService;


    @AutoLog(value = "草稿箱-分页列表查询")
    @ApiOperation(value = "草稿箱-分页列表查询", notes = "草稿箱-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ZsEmailVO>> queryPageList(ZsEmailVO zsEmailVO,
                                                      @RequestParam(name = "keyword", required = false) String keyword,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        Result<IPage<ZsEmailVO>> result = new Result<>();
        Page<ZsEmailVO> page = new Page<ZsEmailVO>(pageNo, pageSize);
        IPage<ZsEmailVO> pageList = zsEmailService.searchDrafts(page, zsEmailVO, keyword);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "草稿箱-通过id删除")
    @ApiOperation(value = "草稿箱-通过id删除", notes = "草稿箱-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            ZsEmail zsEmail = new ZsEmail();
            zsEmail.setId(id);
            zsEmail.setDelFlag(2);
            zsEmailService.updateStatus(zsEmail);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    @AutoLog(value = "草稿箱-批量删除")
    @ApiOperation(value = "草稿箱-批量删除", notes = "草稿箱-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ZsEmailVO> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsEmailVO> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            List<String> list = Arrays.asList(ids.split(","));
            for (int i = 0; i < list.size(); i++) {
                ZsEmail zsEmail = new ZsEmail();
                zsEmail.setId(list.get(i));
                zsEmail.setDelFlag(2);
                zsEmailService.updateStatus(zsEmail);
            }
            result.success("删除成功!");
        }
        return result;
    }

    @AutoLog(value = "草稿箱-通过id查询")
    @ApiOperation(value = "草稿箱-通过id查询", notes = "草稿箱-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ZsEmailVO> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ZsEmailVO> result = new Result<>();
        ZsEmailVO zsEmailVO = zsEmailService.findById(id);
        if (zsEmailVO == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(zsEmailVO);
            result.setSuccess(true);
        }
        return result;
    }

    @AutoLog(value = "草稿箱-编辑")
    @ApiOperation(value = "草稿箱-编辑", notes = "草稿箱-编辑")
    @GetMapping(value = "/edit")
    public Result<ZsEmailVO> edit(@RequestParam(name = "id", required = true) String id) {
        Result<ZsEmailVO> result = new Result<>();
        ZsEmailVO zsEmailVO = zsEmailService.findById(id);
        if (zsEmailVO == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(zsEmailVO);
            result.setSuccess(true);
        }
        return result;
    }


}
