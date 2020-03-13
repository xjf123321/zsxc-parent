package com.zs.create.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsDocTemplate;
import com.zs.create.modules.system.service.ZsDocTemplatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * <p>
 * 公文模板表 前端控制器
 * </p>
 * @Author yaochao
 * @since 2019-10-22
 */
@RestController
@Api(tags = "公文模板")
@RequestMapping("/doctemplate")
@Slf4j
public class ZsDocTemplateController {

    @Autowired
    private ZsDocTemplatService zsDocTemplatService;

    @AutoLog(value = "公文模板-分页列表查询")
    @ApiOperation(value = "公文模板-分页列表查询", notes = "公文模板-分页列表查询")
    @GetMapping("/list")
    public Result<IPage<ZsDocTemplate>> list(ZsDocTemplate zsDocTemplate,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             HttpServletRequest req) {
        Result<IPage<ZsDocTemplate>> result = new Result<>();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        QueryWrapper<ZsDocTemplate> queryWrapper = QueryGenerator.initQueryWrapper(zsDocTemplate, req.getParameterMap());
        Page<ZsDocTemplate> page = new Page<ZsDocTemplate>(pageNo, pageSize);
        IPage<ZsDocTemplate> pageList = zsDocTemplatService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "公文模板-删除模板")
    @ApiOperation(value = "公文模板-删除模板", notes = "公文模板-删除模板")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(value = "id", required = true) String id) {
        try {
            zsDocTemplatService.removeDoc(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    @AutoLog(value = "公文模板-添加模板")
    @ApiOperation(value = "公文模板-添加模板", notes = "公文模板-添加模板")
    @PostMapping("/add")
    public Result<ZsDocTemplate> add(@RequestBody JSONObject jsonObject) throws IOException {
        Result<ZsDocTemplate> result = new Result<>();
        try {
            zsDocTemplatService.saveDoc(jsonObject);
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
    @AutoLog(value = "查询所有公文模板")
    @ApiOperation(value = "查询所有公文模板", notes = "查询所有公文模板")
    @RequestMapping(value = "/findAllByStatus", method = RequestMethod.GET)
    public Result<List<ZsDocTemplate>> findAllByStatus() {
        Result<List<ZsDocTemplate>> result = new Result<>();
        QueryWrapper<ZsDocTemplate> qw = new QueryWrapper<>();
        qw.eq("del_flag", "0");
        List<ZsDocTemplate> list = zsDocTemplatService.list(qw);
        if (list.size() == 0) {
            result.error500("公告模板暂时为空");
        }
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }


}
