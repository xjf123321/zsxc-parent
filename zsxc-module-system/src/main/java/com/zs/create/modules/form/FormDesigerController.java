package com.zs.create.modules.form;

import com.zs.create.common.api.vo.Result;
import com.zs.create.modules.form.entity.FormControls;
import com.zs.create.modules.form.entity.FormDesiger;
import com.zs.create.modules.form.entity.FormOnlineRel;
import com.zs.create.modules.form.service.IFormDesigerService;
import com.zs.create.modules.form.service.IOnlineFormService;
import com.zs.create.vo.TPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description 表单设计Controller层
 * @Author HeLiu
 * @Date 2019/8/29 15:19
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/form/formdesiger")
@Api(tags = "表单设计")
public class FormDesigerController {

    @Autowired
    private IFormDesigerService formDesigerService;

    @Autowired
    private IOnlineFormService onlineFormService;

    /**
     * @Description 新增（绑定online表单）
     * @Author HeLiu
     * @Date 2019/8/29 15:37
     **/
    @PostMapping(value = "/add")
    @ApiOperation("表单设计新增")
    public Result<T> add(@RequestBody FormOnlineRel formOnlineRel) {
        Result<T> result = new Result<T>();
        try {
            formDesigerService.add(formOnlineRel);
            result.success("添加成功！");
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @Description 列表
     * @Author HeLiu
     * @Date 2019/8/29 10:44
     **/
    @GetMapping(value = "/list")
    @ApiOperation("表单设计列表")
    public Result<TPage<FormOnlineRel>> queryPageList(@RequestParam(name = "keyword", required = false) String keyword,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<TPage<FormOnlineRel>> result = new Result<>();
        TPage<FormOnlineRel> pageList = formDesigerService.list(keyword, pageNo, pageSize);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @Description onlineForm绑定列表
     * @Author HeLiu
     * @Date 2019/9/3 10:45
     **/
    @GetMapping(value = "/getOnlineFormList")
    @ApiOperation("OnlineForm绑定列表")
    public Result<List<Map>> getOnlineFormList() {
        Result<List<Map>> result = new Result<>();
        List<Map> results = formDesigerService.getOnlineFormList();
        result.setSuccess(true);
        result.setResult(results);
        return result;
    }

    /**
     * @Description 编辑
     * @Author HeLiu
     * @Date 2019/8/29 14:00
     **/
    @PutMapping(value = "/edit")
    @ApiOperation("表单设计编辑")
    public Result<T> edit(@RequestBody FormOnlineRel formOnlineRel) {
        Result<T> result = new Result<T>();
        formDesigerService.edit(formOnlineRel);
        result.success("修改成功!");
        return result;
    }

    /**
     * @Description 删除
     * @Author HeLiu
     * @Date 2019/8/29 11:42
     **/
    @DeleteMapping(value = "/delete")
    @ApiOperation("表单设计删除")
    public Result<T> delete(@RequestParam(name = "id", required = true) String id) {
        Result<T> result = new Result<T>();
        FormOnlineRel formOnlineRel = formDesigerService.getById(id);
        if (formOnlineRel == null) {
            result.error500("未找到对应实体");
        } else {
            formDesigerService.delById(id);
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * @Description 设计表单数据保存
     * @Author HeLiu
     * @Date 2019/8/31 10:49
     **/
    @PostMapping(value = "/addOrEditData")
    @ApiOperation("设计表单数据保存")
    public Result<T> addOrEditData(@RequestBody FormDesiger formDesiger) {
        Result<T> result = new Result<T>();
        try {
            String formOnlineRelId = formDesiger.getFormOnlineRelId();
            Map<String, Object> formDesigerData = formDesigerService.getFormDesigerData(formOnlineRelId);
            if (null == formDesigerData || CollectionUtils.isEmpty(formDesigerData)) {
                //新增
                formDesigerService.addFormDesigerData(formDesiger);
            } else {
                //修改
                formDesigerService.editFormDesigerData(formDesiger);
            }
            result.success("保存成功！");
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @Description 设计表单数据
     * @Author HeLiu
     * @Date 2019/9/4 12:52
     **/
    @GetMapping(value = "/getFormDesigerData")
    @ApiOperation("设计表单数据")
    public Result<Map<String, Object>> getFormDesigerData(@RequestParam(name = "formOnlineRelId") String formOnlineRelId) {
        Result<Map<String, Object>> result = new Result<>();
        try {
            Map<String, Object> formDesigerData = formDesigerService.getFormDesigerData(formOnlineRelId);
            result.setResult(formDesigerData);
            result.success("操作成功！");
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @Description 控件数据
     * @Author HeLiu
     * @Date 2019/9/2 8:50
     **/
    @GetMapping(value = "/formControlsData")
    @ApiOperation("控件数据")
    public Result<List<FormControls>> formControlsData(@RequestParam(name = "onlineTableName") String onlineTableName) {
        Result<List<FormControls>> result = new Result<>();
        try {
            List<FormControls> formControlsList = onlineFormService.getFormControlsData(onlineTableName);
            result.setResult(formControlsList);
            result.success("操作成功！");
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


}
