package com.zs.create.modules.zdemo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.common.util.oConvertUtils;
import com.zs.create.modules.zdemo.entity.DemoEntity;
import com.zs.create.modules.zdemo.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author heliu
 * @Description 测试Controller层
 * @email heliu@zs-create.com
 * @date 2019-08-30 10:23:20
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "测试")
@RestController
@RequestMapping("/zdemo/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "测试-分页列表查询")
    @ApiOperation(value = "测试-分页列表查询", notes = "测试-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<DemoEntity>> queryPageList(DemoEntity demo,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        Result<IPage<DemoEntity>> result = new Result<IPage<DemoEntity>>();
        QueryWrapper<DemoEntity> queryWrapper = QueryGenerator.initQueryWrapper(demo, req.getParameterMap());
        Page<DemoEntity> page = new Page<DemoEntity>(pageNo, pageSize);
        IPage<DemoEntity> pageList = demoService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     */
    @AutoLog(value = "测试-添加")
    @ApiOperation(value = "测试-添加", notes = "测试-添加")
    @PostMapping(value = "/add")
    public Result<DemoEntity> add(@RequestBody DemoEntity demo) {
        Result<DemoEntity> result = new Result<DemoEntity>();
        try {
            demoService.save(demo);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     */
    @AutoLog(value = "测试-编辑")
    @ApiOperation(value = "测试-编辑", notes = "测试-编辑")
    @PutMapping(value = "/edit")
    public Result<DemoEntity> edit(@RequestBody DemoEntity demo) {
        Result<DemoEntity> result = new Result<DemoEntity>();
        DemoEntity demoEntity = demoService.getById(demo.getId());
        if (demoEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = demoService.updateById(demo);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }
        return result;
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "测试-通过id删除")
    @ApiOperation(value = "测试-通过id删除", notes = "测试-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            demoService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "测试-批量删除")
    @ApiOperation(value = "测试-批量删除", notes = "测试-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<DemoEntity> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<DemoEntity> result = new Result<DemoEntity>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.demoService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "测试-通过id查询")
    @ApiOperation(value = "测试-通过id查询", notes = "测试-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<DemoEntity> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<DemoEntity> result = new Result<DemoEntity>();
        DemoEntity demo = demoService.getById(id);
        if (demo == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(demo);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<DemoEntity> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                DemoEntity demo = JSON.parseObject(deString, DemoEntity.class);
                queryWrapper = QueryGenerator.initQueryWrapper(demo, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<DemoEntity> pageList = demoService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "测试列表");
        mv.addObject(NormalExcelConstants.CLASS, DemoEntity.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("测试列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue(); // 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<DemoEntity> listDemos = ExcelImportUtil.importExcel(file.getInputStream(), DemoEntity.class, params);
                demoService.saveBatch(listDemos);
                return Result.ok("文件导入成功！数据行数:" + listDemos.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "多数据源测试-通过id查询")
    @ApiOperation(value = "多数据源测试-通过id查询", notes = "多数据源测试-通过id查询")
    @GetMapping(value = "/mulQueryById")
    public Result mulQueryById(@RequestParam(name = "id", required = true) String id) {
        Result result = new Result();
        Map obj = demoService.mulQueryById(id);
        if (CollectionUtils.isEmpty(obj)) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(obj);
            result.setSuccess(true);
        }
        return result;
    }


    /**
     * 事务测试
     */
    @AutoLog(value = "事务测试")
    @ApiOperation(value = "事务测试", notes = "事务测试")
    @GetMapping(value = "/transaction")
    public Result transaction(@RequestParam(name = "id", required = true) String id) {
        Result result = new Result();
        try {
            demoService.transaction(id);
            result.setSuccess(true);
            result.setMessage("数据库操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("数据库操作异常，启动回滚!");
        }
        return result;
    }

}

