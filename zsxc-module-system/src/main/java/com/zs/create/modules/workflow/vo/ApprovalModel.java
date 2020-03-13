package com.zs.create.modules.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @Auther: lr
 * @Date: 2019/8/7 14:33
 * @Description:审批记录
 */
@Data
@Accessors(chain = true)
@ApiModel("流程审批")
public class ApprovalModel {

    /**
     * 主键ID
     */
    @ApiParam(name = "主键ID", example = "AAAAA")
    private String id = UUID.randomUUID().toString().replace("-", "");
    /**
     * taskID
     */
    @ApiParam(name = "任务ID", example = "AAAAA")
    private String taskId;
    /**
     * 申请ID
     */
    @ApiParam(name = "申请ID", example = "AAAAA")
    private String applyId;
    /**
     * 流程节点名称
     */
    @ApiParam(name = "流程节点名称", example = "BBBBB")
    private String flowNodeName;
    /**
     * 审批用户ID
     */
    @ApiParam(name = "审批用户ID", example = "CCCC")
    private String userId;
    /**
     * 审批时间
     */
    @ApiParam(name = "审批时间", example = "CCCC")
    private String time;
    /**
     * 审批意见
     */
    @ApiParam(name = "审批意见", example = "CCCC")
    private String idea;
    /**
     * 审批备注
     */
    @ApiParam(name = "审批备注", example = "CCCC")
    private String note;


    /**
     * 参数校验
     *
     * @return
     */
    public String checkParams() {
        String message = StringUtils.EMPTY;
        if (StringUtils.isEmpty(taskId)) {
            message = "任务ID不能为空";
        } else if (StringUtils.isEmpty(applyId)) {
            message = "申请ID不能为空";
        } else if (StringUtils.isEmpty(flowNodeName)) {
            message = "任务节点名称不能为空";
        } else if (StringUtils.isEmpty(idea)) {
            message = "审批意见不能为空";
        }
        return message;
    }

}
