package com.zs.create.modules.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsNoticeTemplate;
import com.zs.create.modules.system.service.ZsNoticeTemplatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * <p>
 * 通知模板表 前端控制器
 * </p>
 * @Author yaochao
 * @since 2019-10-22
 */
@RestController
@Api(tags = "公告模板")
@RequestMapping("/noticeTemplate")
@Slf4j
public class ZsNoticeTemplateController {

    @Autowired
    private ZsNoticeTemplatService zsNoticeTemplatService;

    @AutoLog(value = "通告模板-分页列表查询")
    @ApiOperation(value = "通告模板-分页列表查询", notes = "通告模板-分页列表查询")
    @GetMapping("/list")
    public Result<IPage<ZsNoticeTemplate>> list(ZsNoticeTemplate zsNoticeTemplate,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        Result<IPage<ZsNoticeTemplate>> result = new Result<>();
        QueryWrapper<ZsNoticeTemplate> queryWrapper = QueryGenerator.initQueryWrapper(zsNoticeTemplate, req.getParameterMap());
        Page<ZsNoticeTemplate> page = new Page<>(pageNo, pageSize);
        IPage<ZsNoticeTemplate> pageList = zsNoticeTemplatService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "通告模板-删除模板")
    @ApiOperation(value = "通告模板-删除模板", notes = "通告模板-删除模板")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(value = "id", required = true) String id) {
        try {
            zsNoticeTemplatService.removeNotice(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    @AutoLog(value = "通告模板-添加模板")
    @ApiOperation(value = "通告模板-添加模板", notes = "通告模板-添加模板")
    @PostMapping("/add")
    public Result<ZsNoticeTemplate> add(@RequestBody JSONObject jsonObject) throws IOException {
        Result<ZsNoticeTemplate> result = new Result<>();
        try {
            zsNoticeTemplatService.saveNotice(jsonObject);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
     * 查询所有公告模板
     * */
    @AutoLog(value = "查询所有公告模板")
    @ApiOperation(value = "查询所有公告模板", notes = "查询所有公告模板")
    @RequestMapping(value = "/findAllByStatus", method = RequestMethod.GET)
    public Result<List<ZsNoticeTemplate>> findAllByStatus() {
        Result<List<ZsNoticeTemplate>> result = new Result<>();
        QueryWrapper<ZsNoticeTemplate> qw = new QueryWrapper<>();
        qw.eq("del_flag", "0");
        List<ZsNoticeTemplate> list = zsNoticeTemplatService.list(qw);
        if (list.size() == 0) {
            result.error500("公告模板暂时为空");
        }
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }


}
