package com.zs.create.modules.workflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.workflow.vo.ApprovalModel;
import com.zs.create.modules.workflow.vo.BpmnFormlMap;
import com.zs.create.modules.workflow.vo.BpmnWork;
import com.zs.create.vo.TPage;

import java.util.Map;

/**
 * @Description 工作流
 * @Author lr
 * @Date 2019/8/28 10:48
 * @Version 1.0
 **/
public interface IWorkflowModelService extends IService<BpmnWork> {

    /**
     * 新增
     *
     * @param modelHandler
     */
    void add(Map modelHandler);

    /**
     * 列表
     *
     * @param page
     * @return
     */
    TPage<Map> list(TPage<Map> page);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Map<String, Object> getById(String id);

    /**
     * 根据id删除
     *
     * @param id
     */
    void delById(String id);

    /**
     * 查询历史版本
     *
     * @param page
     * @return
     */
    TPage listVersions(TPage<Map> page);

    /**
     * 流程启动
     *
     * @param formData
     * @return
     */
    void workApply(BpmnFormlMap formData);

    /**
     * 我的任务列表
     *
     * @param username
     * @param page
     * @return
     */
    IPage<BpmnWork> workList(String username,String emergency_level,String deptId,String parentDetId,IPage<BpmnWork> page);

    /**
     * 我的任务列表
     *
     * @param taskId
     * @param applyId
     * @param formDataId
     * @return
     */
    Map formShow(String taskId, String applyId, String formDataId,String workId,String sqId);

    /**
     * 提交
     *
     * @param model
     */
    void workSubmit(ApprovalModel model);

    /**
     * 驳回
     *
     * @param model
     */
    void workReject(ApprovalModel model);

    /**
     * 终止
     *
     * @param model
     */
    void workStop(ApprovalModel model);

    /**
     * 任务拾取
     *
     * @param taskId
     */
    void workClaim(String taskId);

}
