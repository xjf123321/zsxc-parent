package com.zs.create.modules.workflow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.mapper.SysDepartMapper;
import com.zs.create.modules.system.mapper.SysUserDepartMapper;
import com.zs.create.modules.system.service.ISysUserService;
import com.zs.create.modules.workflow.assignee.service.IAssigneeService;
import com.zs.create.modules.workflow.common.enums.ActionTypeEumn;
import com.zs.create.modules.workflow.service.IWorkflowModelService;
import com.zs.create.modules.workflow.vo.ApprovalModel;
import com.zs.create.modules.workflow.vo.BpmnFormlMap;
import com.zs.create.modules.workflow.vo.BpmnWork;
import com.zs.create.modules.workflow.vo.WorkflowUser;
import com.zs.create.vo.TPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description WorkflowController
 * @Author lr
 * @Date 2019/8/29 10:16
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/workflow")
@Api(tags = "工作流流程管理")
public class WorkflowController {

    @Autowired
    private IWorkflowModelService workflowModelService;

    @Autowired
    private IAssigneeService assigneeService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    /**
     * 流程部署
     *
     * @param modelEntity
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation("流程部署")
    public Result<T> add(@RequestBody Map modelEntity) {
        Result<T> result = new Result<T>();
        if (CollectionUtils.isEmpty(modelEntity)) {
            result.error500("参数不能为空!");
            return result;
        }
        if (null == modelEntity.get("processKey")) {
            result.error500("参数processKey不能为空!");
            return result;
        }
        try {
            //获取登录用户信息
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            modelEntity.put("create_by", sysUser.getId());
            workflowModelService.add(modelEntity);
            result.success("添加成功！");
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            result.error500(e.getMessage());
        }
        return result;
    }

    /**
     * @Description 列表
     * @Author HeLiu
     * @Date 2019/8/29 10:44
     **/
    @GetMapping(value = "/list")
    @ApiOperation("流程列表")
    public Result<TPage<Map>> queryPageList(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "key", required = false) String key,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<TPage<Map>> result = new Result<>();
        TPage<Map> page = new TPage<>();
        Map<String, Object> params = new HashMap<>(1);
        params.put("processName", name);
        params.put("processKey", key);
        page.setParams(params);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        TPage<Map> pageList = workflowModelService.list(page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 查询历史版本
     *
     * @param key
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/versionList")
    @ApiOperation("流程历史版本列表")
    public Result<TPage<Map>> versionList(@RequestParam(name = "key") @ApiParam(value = "流程的processKey") String key,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<TPage<Map>> result = new Result<>();
        TPage<Map> page = new TPage<>();
        Map<String, Object> params = new HashMap<>(1);
        params.put("processKey", key);
        page.setParams(params);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        TPage<Map> pageList = workflowModelService.listVersions(page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 流程删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation("流程删除")
    public Result<T> delete(@RequestParam(name = "id") String id) {
        Result<T> result = new Result<>();
        workflowModelService.delById(id);
        result.success("删除成功!");
        return result;
    }

    /**
     * 查询流程信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation("流程信息")
    public Result queryById(@RequestParam(name = "id", required = true) String id) {
        Result result = new Result<>();
        Map<String, Object> workflow = workflowModelService.getById(id);
        if (CollectionUtils.isEmpty(workflow)) {
            result.error500("未找到指定数据!");
        } else {
            workflow.remove("_id");
            result.setResult(workflow);
            result.success("查询成功!");
        }
        return result;
    }

    /**
     * 查询按钮动作类型
     *
     * @return
     */
    @GetMapping(value = "/queryBtActions")
    @ApiOperation("按钮动作类型")
    public Result<Map<String, Object>> queryBtActions() {
        Result result = new Result<>();
        List list = Lists.newArrayList();
        Arrays.stream(ActionTypeEumn.values()).forEach(item -> list.add(new HashMap(2) {{
            put("label", item.getDes());
            put("value", item.getCode());
        }}));
        result.setResult(list);
        result.success("查询成功!");
        return result;
    }

    /**
     * 我的任务列表
     *
     * @param realname
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/workList")
    @ApiOperation("我的任务列表")
    public Result<IPage<BpmnWork>> workList(@RequestParam(name = "realname", required = false) String realname,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<BpmnWork>> result = new Result<>();
        Page<BpmnWork> page = new Page<>(pageNo, pageSize);
        String emergency_level = null;
        String deptId = null;
        String parentDetId = null;
        IPage<BpmnWork> pageList = workflowModelService.workList(realname,emergency_level,deptId,parentDetId,page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 获取表单数据与结构
     *
     * @param taskId
     * @param
     * @return
     */
    @GetMapping(value = "/formShow")
    @ApiOperation("我的任务列表")
    public Result formShow(@RequestParam(name = "taskId", required = false) String taskId,
                           @RequestParam(name = "applyId", required = false) String applyId,
                           @RequestParam(name = "workId", required = false) String workId,
                           @RequestParam(name = "sqId", required = false) String sqId/*,
                           @RequestParam(name = "formDataId", required = false) String formDataId*/) {
        Result result = new Result<>();
        String formDataId= "daba06c6542241b3bbce2a758d8e2cb2";
        if (StringUtils.isEmpty(taskId)) {
            result.error500("任务ID参数不能为空!");
        } else if (StringUtils.isEmpty(formDataId)) {
            result.error500("表单数据ID参数不能为空!");
        } else if (StringUtils.isEmpty(applyId)) {
            result.error500("申请ID参数不能为空!");
        } else if (StringUtils.isEmpty(workId)) {
            result.error500("工作流workId参数不能为空!");
        } else if (StringUtils.isEmpty(sqId)) {
            result.error500("表单数据sqId参数不能为空!");
        } else {
            try {
                Map map = workflowModelService.formShow(taskId, applyId, formDataId,workId,sqId);
                result.setSuccess(true);
                result.setResult(map);
            } catch (Exception e) {
                result.error500(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 流程申请
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/workApply")
    @ApiOperation("流程申请")
    public Result<T> workApply(@RequestBody(required = true)
                               @ApiParam(name = "流程申请参数", example = "{'workId':'流程ID','formId':'表单ID','businessName':'业务名称','formData':{表单数据json}}") BpmnFormlMap param) {
        Result<T> result = new Result<T>();
        if (CollectionUtils.isEmpty(param)) {
            result.error500("流程启动参数不能为空!");
            return result;
        }
        String workId = (String) param.get("workId");
        String formId = (String) param.get("formId");
        if (StringUtils.isEmpty(workId)) {
            result.error500("流程ID参数不能为空!");
        } else if (StringUtils.isEmpty(formId)) {
            result.error500("表单ID参数不能为空!");
        } else {
            try {
                workflowModelService.workApply(param);
                result.success("启动成功！");
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                result.error500(e.getMessage());
            }
        }
        return result;
    }


    /**
     * 流程提交
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/workSubmit")
    @ApiOperation("流程提交")
    public Result<T> workSubmit(@RequestBody(required = true) ApprovalModel model) {
        Result<T> result = new Result<T>();
        String checkMes = model.checkParams();
        if (!StringUtils.isEmpty(checkMes)) {
            result.error500(checkMes);
            return result;
        }
        try {
            workflowModelService.workSubmit(model);
            result.setSuccess(true);
        } catch (Exception e) {
            result.error500(e.getMessage());
        }
        return result;
    }


    /**
     * 流程驳回
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/workReject")
    @ApiOperation("流程驳回")
    public Result<T> workReject(@RequestBody(required = true) ApprovalModel model) {
        Result<T> result = new Result<T>();
        String checkMes = model.checkParams();
        if (!StringUtils.isEmpty(checkMes)) {
            result.error500(checkMes);
            return result;
        }
        try {
            workflowModelService.workReject(model);
            result.setSuccess(true);
        } catch (Exception e) {
            result.error500(e.getMessage());
        }
        return result;
    }

    /**
     * 流程终止
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/workStop")
    @ApiOperation("流程终止")
    public Result<T> workStop(@RequestBody(required = true) ApprovalModel model) {
        Result<T> result = new Result<T>();
        String checkMes = model.checkParams();
        if (!StringUtils.isEmpty(checkMes)) {
            result.error500(checkMes);
            return result;
        }
        try {
            workflowModelService.workStop(model);
            result.setSuccess(true);
        } catch (Exception e) {
            result.error500(e.getMessage());
        }
        return result;
    }


    /**
     * 任务拾取
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/workClaim")
    @ApiOperation("任务拾取")
    public Result<T> workClaim(@RequestParam(name = "taskId", required = false) String taskId) {
        Result<T> result = new Result<T>();
        if (StringUtils.isEmpty(taskId)) {
            result.error500("任务ID不能为空！");
            return result;
        }
        try {
            workflowModelService.workClaim(taskId);
            result.setSuccess(true);
        } catch (Exception e) {
            result.error500(e.getMessage());
        }
        return result;
    }


    /**
     * @Description 个人/候选人
     * @Author HeLiu
     * @Date 2019/9/25 16:35
     **/
    @GetMapping(value = "/queryCandidatePersonal")
    @ApiOperation("个人/候选人")
    public Result<TPage<WorkflowUser>> queryCandidatePersonal(WorkflowUser workflowUser,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<WorkflowUser> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = sysUserService.queryCandidatePersonal(workflowUser, pageNo, pageSize);
        page.setRecords((List<WorkflowUser>) resultMap.get("list"));
        page.setTotal((Long) resultMap.get("total"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /**
     * @Description 候选组
     * @Author HeLiu
     * @Date 2019/9/25 16:38
     **/
    @GetMapping(value = "/queryCandidateSet")
    @ApiOperation("候选组")
    public Result<TPage<Map>> queryCandidateSet(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        TPage<Map> page = new TPage<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Map<String, Object> resultMap = assigneeService.queryGroupList((pageNo - 1) * pageSize, pageNo * pageSize);
        page.setRecords((List<Map>) resultMap.get("groupList"));
        page.setTotal((Long) resultMap.get("count"));
        Result result = new Result<>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    @GetMapping(value = "/workNeed")
    @ApiOperation("代办")
    public Result<IPage<BpmnWork>> workNeed(@RequestParam(name = "realname", required = false) String realname,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<BpmnWork>> result = new Result<>();
        Page<BpmnWork> page = new Page<>(pageNo, pageSize);
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);
        Long count = sysDepartMapper.selectId(deptId);
        String parentDetId = null;
        if (count != 0) {
            parentDetId = deptId;
        }
        String emergency_level = "0";
        IPage<BpmnWork> pageList = workflowModelService.workList(realname,emergency_level,deptId,parentDetId,page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @GetMapping(value = "/workUrgent")
    @ApiOperation("催办")
    public Result<IPage<BpmnWork>> workUrgent(@RequestParam(name = "realname", required = false) String realname,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<BpmnWork>> result = new Result<>();
        Page<BpmnWork> page = new Page<>(pageNo, pageSize);
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);
        Long count = sysDepartMapper.selectId(deptId);
        String parentDetId = null;
        if (count != 0) {
            parentDetId = deptId;
        }
        String emergency_level = "1";
        IPage<BpmnWork> pageList = workflowModelService.workList(realname,emergency_level,deptId,parentDetId,page);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
}
