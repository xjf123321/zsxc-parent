package com.zs.create.modules.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: guodl
 * @Date: 2019/8/7 14:33
 * @Description:bpmn模型操作
 */
@Data
@Accessors(chain = true)
@Document(collection = "workflow_form")
@ApiModel("流程数据表单")
public class BpmnFormlMap extends HashMap {

    /**
     * 流程ID
     */
    @ApiParam(name = "流程ID", example = "AAAAA")
    private String workId;
    /**
     * 表单ID 对应原始表单结构
     */
    @ApiParam(name = "表单ID", example = "BBBBB")
    private String formId;
    /**
     * 表单数据ID
     */
    @ApiParam(name = "表单数据ID", example = "CCCC")
    private String businessFormDataId;
    /**
     * 业务名称
     */
    @ApiParam(name = "业务名称", example = "CCCC")
    private String businessName;
    /**
     * 表单数据
     */
    @ApiParam(name = "表单数据Json", example = "{}")
    private Map formData;

    public BpmnFormlMap() {
        this.put("delFlag", "0");
        this.put("version", 1);
        this.put("createTime", new Date());
        this.put("updateTime", new Date());
        this.put("businessFormDataId", UUID.randomUUID().toString().replace("-", ""));
    }

    public void setWorkId(String workId) {
        this.put("workId", workId);
    }

    public void setFormId(String formId) {
        this.put("formId", formId);
    }

    public void setFormData(Map formData) {
        this.put("formData", formData);
    }

}
