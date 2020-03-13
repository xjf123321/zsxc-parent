package com.zs.create.modules.script.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.modules.script.entity.Script;
import com.zs.create.modules.script.service.IScriptService;
import com.zs.create.modules.script.service.impl.GroovyScriptEngine;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 脚本Controller层
 * @Author HeLiu
 * @Date 2019/9/5 9:51
 * @Version 1.0
 **/
@Slf4j
@Api(tags = "脚本管理")
@RestController
@RequestMapping("/sys/script")
public class ScriptController {

    @Autowired
    private IScriptService scriptService;

    @Autowired
    private GroovyScriptEngine groovyScriptEngine;

    /**
     * @Description 新增
     * @Author HeLiu
     * @Date 2019/9/5 10:32
     **/
    @PostMapping(value = "/add")
    @ApiOperation("脚本新增")
    public Result<Script> add(@RequestBody Script script) {
        Result<Script> result = new Result<Script>();
        try {
            scriptService.save(script);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @Description 列表
     * @Author HeLiu
     * @Date 2019/9/5 10:54
     **/
    @GetMapping(value = "/list")
    @ApiOperation("脚本列表")
    public Result<IPage<Script>> queryPageList(Script script, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<Script>> result = new Result<IPage<Script>>();
        QueryWrapper<Script> queryWrapper = QueryGenerator.initQueryWrapper(script, req.getParameterMap());
        Page<Script> page = new Page<Script>(pageNo, pageSize);
        IPage<Script> pageList = scriptService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @Description 编辑
     * @Author HeLiu
     * @Date 2019/9/5 10:57
     **/
    @PutMapping(value = "/edit")
    @ApiOperation("脚本编辑")
    public Result<Script> edit(@RequestBody Script script) {
        Result<Script> result = new Result<Script>();
        Script scriptResult = scriptService.getById(script.getId());
        if (scriptResult == null) {
            result.error500("未找到对应实体");
        } else {
            script.setUpdateTime(new Date());
            boolean ok = scriptService.updateById(script);
            if (ok) {
                result.success("编辑成功!");
            }
        }
        return result;
    }

    /**
     * @Description 删除
     * @Author HeLiu
     * @Date 2019/9/5 11:01
     **/
    @DeleteMapping(value = "/delete")
    @ApiOperation("脚本删除")
    public Result<Script> delete(@RequestParam(name = "id", required = true) String id) {
        Result<Script> result = new Result<Script>();
        Script script = new Script();
        script.setId(id);
        script.setDelFlag("1");
        script.setUpdateTime(new Date());
        boolean ok = scriptService.updateById(script);
        if (ok) {
            result.success("删除成功!");
        } else {
            result.error500("删除失败!");
        }
        return result;
    }

    /**
     * @Description 下拉框列表
     * @Author HeLiu
     * @Date 2019/9/6 9:27
     **/
    @GetMapping(value = "/selectList")
    @ApiOperation("脚本下拉框列表")
    public Result<List<Script>> selectList() {
        Result<List<Script>> result = new Result<List<Script>>();
        List<Script> selectList = scriptService.list();
        result.setSuccess(true);
        result.setResult(selectList);
        return result;
    }

    /**
     * @Description 执行系统脚本
     * @Author HeLiu
     * @Date 2019/9/6 16:14
     **/
    @GetMapping(value = "/executeScript")
    @ApiOperation("执行系统脚本")
    public Result<Object> executeScript(@RequestParam(name = "scriptExpression") String scriptExpression) throws Exception {
        Result<Object> result = new Result<Object>();
        Object obj = groovyScriptEngine.executeObject(scriptExpression, new HashMap<>());
        if (StringUtils.isBlank(ObjectUtils.toString(obj, ""))) {
            result.error500("执行系统脚本失败！");
        } else {
            if (obj instanceof List) {
                List<Object> list = (List<Object>) obj;
                if (list == null || list.size() <= 0) {
                    result.error500("执行系统脚本失败！");
                } else {
                    result.setSuccess(true);
                    result.setResult(obj);
                }
            } else if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                if (CollectionUtils.isEmpty(map)) {
                    result.error500("执行系统脚本失败！");
                } else {
                    result.setSuccess(true);
                    result.setResult(obj);
                }
            } else {
                result.setSuccess(true);
                result.setResult(obj);
            }
        }
        return result;
    }

}
