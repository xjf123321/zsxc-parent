package com.zs.create.modules.workflow.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zs.create.common.exception.ZsxcBootException;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.form.service.IFormDesigerService;
import com.zs.create.modules.system.entity.ZsSqBack;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.entity.ZsSqRoom;
import com.zs.create.modules.system.service.ZsSqBackService;
import com.zs.create.modules.system.service.ZsSqCarService;
import com.zs.create.modules.system.service.ZsSqLeaveService;
import com.zs.create.modules.system.service.ZsSqRoomService;
import com.zs.create.modules.workflow.common.bpmn.BpmnModelHandler;
import com.zs.create.modules.workflow.common.entity.BpmnModelEntity;
import com.zs.create.modules.workflow.common.enums.TaskStatusEumn;
import com.zs.create.modules.workflow.deploy.service.IProcessDeployService;
import com.zs.create.modules.workflow.instance.service.IProcessInstanceService;
import com.zs.create.modules.workflow.mapper.BpmWorkMapper;
import com.zs.create.modules.workflow.service.IWorkflowModelService;
import com.zs.create.modules.workflow.task.service.IProcessTaskService;
import com.zs.create.modules.workflow.task.service.impl.YichangProcessTaskService;
import com.zs.create.modules.workflow.vo.*;
import com.zs.create.util.MonogoUtil;
import com.zs.create.vo.TPage;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Description 工作流基础查询
 * @Author lr
 * @Date 2019/8/28 10:48
 * @Version 1.0
 **/
@Service
@Slf4j
public class WorkflowModelServiceImpl extends ServiceImpl<BpmWorkMapper, BpmnWork> implements IWorkflowModelService {

    @Autowired
    private IProcessDeployService processDeployService;

    @Autowired
    private IProcessInstanceService processInstanceService;

    @Autowired
    private IProcessTaskService processTaskService;

    @Autowired
    private IFormDesigerService formDesigerService;

    @Autowired
    private YichangProcessTaskService yichangProcessTaskService;

    @Autowired
    private MonogoUtil monogoUtil;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private BpmWorkMapper workMapper;

    @Autowired
    private ZsSqCarService zsSqCarService;      //车辆

    @Autowired
    private ZsSqRoomService zsSqRoomService;    //会议

    @Autowired
    private ZsSqLeaveService zsSqLeaveService;    //请假

    @Autowired
    private ZsSqBackService zsSqBackService;    //销假


    private static final String WORKFLOW_MODEL = "workflow_model";
    private static final String WORKFLOW_FORM = "workflow_form";


    @Override
    public void add(Map modelEntity) {
        BpmnModelEntity handler = JSON.parseObject(JSON.toJSONString(modelEntity), BpmnModelEntity.class);
        if (null == handler) {
            log.error("流程新增失败,参数转换成BpmnModelEntity发生错误，数据：{}", JSON.toJSONString(modelEntity));
            return;
        }
        BpmnModelHandler modelHandler = new BpmnModelHandler(handler.getProcessKey(), handler.getProcessName(), handler);
        try {
            processDeployService.invokeWorkFlowDeploy(modelHandler);
            //流程数据写入 monogodb
            Integer version = monogoUtil.maxField(new HashMap(1) {{
                put("processKey", handler.getProcessKey());
            }}, WORKFLOW_MODEL, "processKey", "version");
            version = (null != version && version > 0) ? (version + 1) : 1;
            String workId = UUID.randomUUID().toString().replace("-", "");
            BpmnModelMap bmm = new BpmnModelMap(modelEntity, workId, "0", version);
            monogoUtil.save(bmm);
            log.error("流程新增成功,数据：{}", JSON.toJSONString(modelEntity));
        } catch (Exception e) {
            log.error("流程新增失败,数据：{}", JSON.toJSONString(modelEntity));
            throw new ZsxcBootException(e.getMessage(), e);
        }
    }

    @Override
    public TPage<Map> list(TPage<Map> page) {
        long total = monogoUtil.findByGroupCount(page.getParams(), WORKFLOW_MODEL, "processKey");
        AggregationOperation aggOper = Aggregation.group("processKey")
                .first("workId").as("workId")
                .first("processKey").as("processKey")
                .first("processName").as("processName")
                .first("createTime").as("createTime")
                .first("nodeForm").as("nodeForm")
                .first("version").as("version");
        List byGroupPage = monogoUtil.findByGroupPage(page.getParams(), WORKFLOW_MODEL, aggOper, "version", Sort.Direction.DESC, page);
        page.setRecords(byGroupPage);
        page.setTotal(total);
        return page;
    }

    @Override
    public Map getById(String id) {
        return monogoUtil.findOne("workId", id, WORKFLOW_MODEL);
    }

    @Override
    public void delById(String id) {
        Map params = new HashMap(1) {{
            put("delFlag", "1");
        }};
        monogoUtil.update("workId", id, params, WORKFLOW_MODEL);
    }

    @Override
    public TPage listVersions(TPage<Map> page) {
        long total = monogoUtil.count(page.getParams(), WORKFLOW_MODEL);
        AggregationOperation aggOper = Aggregation.project("workId", "processKey", "processName", "version", "createTime", "nodeForm");
        List records = monogoUtil.findByGroupPage(page.getParams(), WORKFLOW_MODEL, aggOper, "version", Sort.Direction.DESC, page);
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void workApply(BpmnFormlMap data) {
        String workId = (String) data.get("workId");
        Map<String, Object> workflow = this.getById(workId);
        BpmnModelEntity handler = JSON.parseObject(JSON.toJSONString(workflow), BpmnModelEntity.class);
        if (null == handler || StringUtils.isEmpty(handler.getProcessKey())) {
            log.info("流程数据格式异常，请检查BpmnModelHandler");
            return;
        }
        //保存表单数据
        monogoUtil.save(data);
        //流程启动
        ProcessInstance processInstance;
        try {
            processInstance = processInstanceService.startProcessInstance(handler.getProcessKey(),(Map<String, java.lang.Object>) data.get("formData"), handler.getEnableTaskListener());
            //保存流程申请与审批数据
            String applySql = "INSERT INTO `business_shenqing` (`id`, `business_form_data_id`, `processinstance_id`, `business_name`, `business_sq_userid`, `business_status`, `business_sqtime`, `del_flag`)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            String approvalSql = "INSERT INTO `task_shenpi` (`id`, `business_shenqing_id`, `flow_node_name`, `shenpi_userid`, `shenpi_time`, `shenpi_yijian`, `note`) VALUES (?, ?, ?, ?, ?, ?, ?);";
            String applyId = UUID.randomUUID().toString().replace("-", "");
            String approvalId = UUID.randomUUID().toString().replace("-", "");
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            template.update(applySql, applyId, data.get("businessFormDataId"),
                    processInstance.getProcessInstanceId(), data.get("businessName"), sysUser.getId(), TaskStatusEumn.SQ.getCode(), new Date(), "0");
            template.update(approvalSql, approvalId, applyId, "开始", sysUser.getId(), new Date(), StringUtils.EMPTY, StringUtils.EMPTY);
        } catch (Exception e) {
            log.error("流程启动失败，数据：{}", JSON.toJSONString(data));
            throw new ZsxcBootException(e.getMessage(), e);
        }
    }

    @Override
    public IPage<BpmnWork> workList(String username, String emergency_level,String deptId,String parentDetId,IPage<BpmnWork> page) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Long count = workMapper.workCount(username,sysUser.getId(),emergency_level,deptId,parentDetId);
        List<BpmnWork> bpmnWorkIPage = workMapper.workList(page, username, sysUser.getId(),emergency_level,deptId,parentDetId);
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    public Map formShow(String taskId, String applyId, String formDataId,String workId,String sqId) {
        Map result = Maps.newHashMap();
        try {
            Map formData = monogoUtil.findOne("businessFormDataId", formDataId, WORKFLOW_FORM);
            if (CollectionUtils.isEmpty(formData)) {
                throw new ZsxcBootException("未找到指定的表单数据");
            }
            String formId = (String) formData.get("formId");
            Map formDesiger = formDesigerService.getFormDesigerData(formId);
            if (CollectionUtils.isEmpty(formDesiger)) {
                throw new ZsxcBootException("未找到指定的表单结构");
            }
            Task task = processTaskService.getTaskById(taskId);
            System.out.println("++++++++++++++"+taskId);
            System.out.println("++++++++++++++"+task);
            String nodeId = task.getTaskDefinitionKey();
            Map workflow = this.getById((String) workId);
            if (CollectionUtils.isEmpty(workflow)) {
                throw new ZsxcBootException("未找到指定的流程记录");
            }
            List<Map> nodeList = (List) workflow.get("nodeList");
            if (!CollectionUtils.isEmpty(nodeList)) {
                for (Map item : nodeList) {
                    if (nodeId.equals(item.get("id"))) {
                        Map info = (Map) item.get("info");
                        result.put("nodeForm", CollectionUtils.isEmpty(info) ? info : info.get("nodeForm"));
                        break;
                    }
                }
            }
            BusinessShenqing businessShenqing = workMapper.selectBySqId(sqId);
            if (businessShenqing.getBusinessName().equals("车辆审批流程")&&businessShenqing.getWorkId().equals(workId)) {
                ZsSqCar objectData = zsSqCarService.selectById(sqId);
                result.put("objectData",objectData);
            }else if(businessShenqing.getBusinessName().equals("会议审批流程")&&businessShenqing.getWorkId().equals(workId)) {
                ZsSqRoom objectData = zsSqRoomService.select(sqId);
                result.put("objectData",objectData);
            }else if(businessShenqing.getBusinessName().equals("请假审批流程")&&businessShenqing.getWorkId().equals(workId)) {
                ZsSqLeave objectData = zsSqLeaveService.selectById(sqId);
                result.put("objectData",objectData);
            }else if(businessShenqing.getBusinessName().equals("销假审批流程")&&businessShenqing.getWorkId().equals(workId)) {
                ZsSqBack objectData = zsSqBackService.selectById(sqId);
                result.put("objectData",objectData);
            }else if(businessShenqing.getBusinessName().equals("公文审批流程")&&businessShenqing.getWorkId().equals(workId)) {

            }
            String historySql = "SELECT t.id as id,t.flow_node_name" +
                    " as flowNodeName,t.shenpi_time as shenpiTime,t.shenpi_yijian as shenpiYijian,t.note as note ,k.realname as realname\n"
                    + "from task_shenpi t LEFT JOIN sys_user k ON t.`shenpi_userid`=k.`id`\n"
                    + "WHERE t.`business_shenqing_id`=? order by t.shenpi_time asc ";
            result.put("history", template.queryForList(historySql, applyId));
            result.put("nodeName", task.getName());
            result.put("formData", formData);
            result.put("formDesiger", formDesiger);
        } catch (Exception e) {
            log.error("表单数据获取失败，任务ID：{}，表单数据ID：{}", taskId, formDataId);
            throw new ZsxcBootException(e.getMessage(), e);
        }
        return result;
    }





    @Override
    @Transactional(rollbackFor = Exception.class)
    public void workSubmit(ApprovalModel model) {
        try {
            TaskStatusEumn status = processTaskService.compeleteTask(model.getTaskId(), new HashMap(1) {{
                put("spyj", model.getIdea());
                //put("userId",);
                put("taskId",model.getTaskId());
            }});
            insertSpAndUpdateSq(model, status);
        } catch (Exception e) {
            log.error("流程提交失败，数据：{}", JSON.toJSONString(model));
            throw new ZsxcBootException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void workReject(ApprovalModel model) {
        try {
            TaskStatusEumn status = yichangProcessTaskService.backProcess(model.getTaskId(), new HashMap(1) {{
                put("spyj", model.getIdea());
            }});
            insertSpAndUpdateSq(model, status);
        } catch (Exception e) {
            log.error("流程提交失败，数据：{}", JSON.toJSONString(model));
            throw new ZsxcBootException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void workStop(ApprovalModel model) {
        try {
            TaskStatusEumn status = yichangProcessTaskService.suspendProcess(model.getTaskId());
            insertSpAndUpdateSq(model, status);
        } catch (Exception e) {
            log.error("流程提交失败，数据：{}", JSON.toJSONString(model));
            throw new ZsxcBootException(e.getMessage(), e);
        }
    }

    @Override
    public void workClaim(String taskId) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        try {
            processTaskService.claimTask(taskId, sysUser.getId());
        } catch (Exception e) {
            log.error("任务拾取失败，任务ID：{}", taskId);
            throw new ZsxcBootException(e.getMessage(), e);
        }
    }

    /**
     * 新增审批记录变更申请记录状态
     *
     * @param model
     * @param status
     */
    private void insertSpAndUpdateSq(ApprovalModel model, TaskStatusEumn status) throws Exception {
        String sql = "INSERT INTO `task_shenpi` (`id`, `business_shenqing_id`, `flow_node_name`, `shenpi_userid`, `shenpi_time`, `shenpi_yijian`, `note`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);\n";
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        template.update(sql, model.getId(), model.getApplyId(), model.getFlowNodeName(), sysUser.getId(), new Date(), model.getIdea(), model.getNote());
        String updateSql = "UPDATE `business_shenqing` SET `business_status`=? WHERE `id`=?;";
        template.update(updateSql, status.getCode(), model.getApplyId());
    }

}
