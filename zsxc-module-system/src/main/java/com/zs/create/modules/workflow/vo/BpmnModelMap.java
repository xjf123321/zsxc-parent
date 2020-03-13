package com.zs.create.modules.workflow.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: guodl
 * @Date: 2019/8/7 14:33
 * @Description:bpmn模型操作
 */
@Data
@Accessors(chain = true)
@Document(collection = "workflow_model")
public class BpmnModelMap extends HashMap {


    public BpmnModelMap(String workId, String delFlag, Integer version) {
        this(new HashMap<>(), workId, delFlag, version);
    }

    public BpmnModelMap(Map param, String workId, String delFlag, Integer version) {
        this.putAll(param);
        this.put("workId", workId);
        this.put("delFlag", StringUtils.isEmpty(delFlag) ? "0" : delFlag);
        this.put("version", null == version ? 1 : version);
        this.put("createTime", new Date());
    }

}
