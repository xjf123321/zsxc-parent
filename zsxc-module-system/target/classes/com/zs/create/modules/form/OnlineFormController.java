package com.zs.create.modules.form;

import com.zs.create.common.api.vo.Result;
import com.zs.create.modules.enums.ControlTypeEnum;
import com.zs.create.modules.enums.FieldTypeEnum;
import com.zs.create.modules.form.entity.OnlineForm;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description OnlineFormController层
 * @Author HeLiu
 * @Date 2019/8/29 10:16
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/form/onlineform")
@Api(tags = "Online表单")
public class OnlineFormController {

    @Autowired
    private IOnlineFormService onlineFormService;

    @Autowired
    private IFormDesigerService formDesigerService;

    /**
     * @Description 新增
     * @Author HeLiu
     * @Date 2019/8/29 10:23
     **/
    @PostMapping(value = "/add")
    @ApiOperation("Online表单新增")
    public Result<T> add(@RequestBody OnlineForm onlineForm) {
        Result<T> result = new Result<T>();
        try {
            onlineFormService.add(onlineForm);
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
    @ApiOperation("Online表单列表")
    public Result<TPage<Map>> queryPageList(@RequestParam(name = "tableName", required = false) String tableName,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<TPage<Map>> result = new Result<>();
        TPage<Map> page = new TPage<>();
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        page.setParams(params);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        TPage<Map> pageList = onlineFormService.listByGroup(page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @Description 查看历史版本列表
     * @Author HeLiu
     * @Date 2019/9/3 9:38
     **/
    @GetMapping(value = "/versionList")
    public Result<TPage<Map<String, Object>>> versionList(@RequestParam(name = "tableName") String tableName,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<TPage<Map<String, Object>>> result = new Result<>();
        TPage<Map<String, Object>> page = new TPage<>();
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        page.setParams(params);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        TPage<Map<String, Object>> pageList = onlineFormService.list(page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @Description 删除
     * @Author HeLiu
     * @Date 2019/8/29 11:42
     **/
    @DeleteMapping(value = "/delete")
    public Result<T> delete(@RequestParam(name = "tableName") String tableName) {
        Result<T> result = new Result<T>();
        onlineFormService.delByTableName(tableName);
        result.success("删除成功!");
        return result;
    }

    /**
     * @Description 根据id查询
     * @Author HeLiu
     * @Date 2019/8/29 13:51
     **/
    @GetMapping(value = "/queryById")
    public Result<Map<String, Object>> queryById(@RequestParam(name = "id", required = true) String id,
                                                 @RequestParam(name = "tableName") String tableName) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> onlineForm = onlineFormService.getById(id);
        //是否绑定
        boolean isOnlineBinding = formDesigerService.isOnlineBinding(tableName);
        if (CollectionUtils.isEmpty(onlineForm) && onlineForm == null) {
            result.error500("未找到对应实体");
        } else {
            onlineForm.put("isOnlineBinding", isOnlineBinding);
            result.setResult(onlineForm);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * @Description online表单是否绑定
     * @Author HeLiu
     * @Date 2019/8/31 14:48
     **/
    @GetMapping(value = "/isOnlineBinding")
    public Result<Boolean> isOnlineBinding(@RequestParam(name = "tableName") String tableName) {
        Result<Boolean> result = new Result<>();
        boolean isOnlineBinding = formDesigerService.isOnlineBinding(tableName);
        result.setResult(isOnlineBinding);
        result.setSuccess(true);
        return result;
    }

    /**
     * @Description 编辑
     * @Author HeLiu
     * @Date 2019/8/29 14:00
     **/
    @PutMapping(value = "/edit")
    public Result<T> edit(@RequestBody OnlineForm onlineForm) {
        Result<T> result = new Result<T>();
        onlineFormService.edit(onlineForm);
        result.success("修改成功!");
        return result;
    }

    /**
     * @Description 字段类型列表
     * @Author HeLiu
     * @Date 2019/9/29 11:23
     **/
    @GetMapping(value = "/fieldTypeList")
    @ApiOperation("字段类型列表")
    public Result<List<Map<String, Object>>> fieldTypeList() {
        Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
        result.setResult(FieldTypeEnum.getList());
        result.success("获取字段类型列表成功!");
        return result;
    }

    /**
     * @Description 控件类型列表
     * @Author HeLiu
     * @Date 2019/9/29 11:37
     **/
    @GetMapping(value = "/controlTypeList")
    @ApiOperation("控件类型列表")
    public Result<List<Map<String, Object>>> controlTypeList() {
        Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
        result.setResult(ControlTypeEnum.getList());
        result.success("获取控件类型列表成功!");
        return result;
    }

}
