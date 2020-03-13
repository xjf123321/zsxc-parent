package com.zs.create.modules.workflow.vo;

import com.zs.create.modules.workflow.common.enums.TaskStatusEumn;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: guodl
 * @Date: 2019/8/7 14:33
 * @Description:bpmn模型操作
 */
@Data
@Accessors(chain = true)
@ApiModel("流程数据")
public class BpmnWork implements Serializable {

    private String taskId;
    private String applyId;
    private String userId;
    private String sqId;
    private String workId;
    private String deptName;
    private String taskStaus;
    private String taskTime;
    private String taskName;
    private String formDataId;
    private String realName;
    private String taskType;

    public String getTaskStaus() {
        return TaskStatusEumn.valueOf(taskStaus).getDes();
    }

}
