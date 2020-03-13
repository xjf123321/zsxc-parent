package com.zs.create.workflow;


import com.alibaba.fastjson.JSON;
import com.zs.create.modules.workflow.common.entity.BpmnModelEntity;
import com.zs.create.modules.workflow.service.IWorkflowModelService;
import com.zs.create.vo.TPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkflowTest {

    @Autowired
    private IWorkflowModelService workflowModelService;


    String jsonStr = "{\n" +
            "  \"id\": \"flow-1566269877000\",\n" +
            "  \"name\": \"流程名称\",\n" +
            "  \"enableTaskListener\": \"true\",\n" +
            "  \"nodeList\": [\n" +
            "    {\n" +
            "      \"id\": \"node-1566269880000\",\n" +
            "      \"name\": \"开始\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"start\",\n" +
            "        \"nodeType\": \"START\",\n" +
            "        \"name\": \"开始\",\n" +
            "        \"title\": \"开始节点\",\n" +
            "        \"filter\": \"start\",\n" +
            "        \"cls\": \"tool start\",\n" +
            "        \"ico\": \"iconfont icon-start\",\n" +
            "        \"yiCHangActions\": []\n" +
            "      },\n" +
            "      \"left\": \"49px\",\n" +
            "      \"top\": \"107px\",\n" +
            "      \"ico\": \"iconfont icon-start\",\n" +
            "      \"show\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"node-1566269881000\",\n" +
            "      \"name\": \"普通\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"comm\",\n" +
            "        \"name\": \"普通\",\n" +
            "        \"nodeType\": \"USER_TASK\",\n" +
            "        \"title\": \"部门经理\",\n" +
            "        \"taskAssignee\": {\n" +
            "          \"activitiId\": \"node-1566269881000\",\n" +
            "          \"activitiName\": \"部门经理\",\n" +
            "          \"assigneeType\": \"Script\",\n" +
            "          \"assigneeVal\": \"sysScript.leaderList()\"\n" +
            "        },\n" +
            "        \"filter\": \"comm\",\n" +
            "        \"cls\": \"tool comm\",\n" +
            "        \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "        \"yiCHangActions\": [],\n" +
            "        \"nodeForm\": {\n" +
            "          \"name\": \"表单名称\",\n" +
            "          \"url\": \"表单地址\",\n" +
            "          \"formType\": \"表单类型\",\n" +
            "          \"buttons\": [\n" +
            "            {\n" +
            "              \"name\": \"提交\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"反对\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"驳回\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"其他\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      \"left\": \"155px\",\n" +
            "      \"top\": \"95px\",\n" +
            "      \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "      \"show\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"node-1566269886000\",\n" +
            "      \"name\": \"网关\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"gate\",\n" +
            "        \"nodeType\": \"EXCLUSIVEGATEWAY\",\n" +
            "        \"name\": \"网关\",\n" +
            "        \"title\": \"分支网关\",\n" +
            "        \"filter\": \"exclude_gateway\",\n" +
            "        \"cls\": \"tool exclude_gateway\",\n" +
            "        \"ico\": \"iconfont icon-exclude_gateway\",\n" +
            "        \"conditions\": [\n" +
            "          {\n" +
            "            \"gateWayId\": \"node-1566269886000\",\n" +
            "            \"to\": \"node-1566269888000\",\n" +
            "            \"title\": \"副总\",\n" +
            "            \"condition\": \"money<300\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"gateWayId\": \"node-1566269886000\",\n" +
            "            \"to\": \"node-1566269891000\",\n" +
            "            \"title\": \"总经理\",\n" +
            "            \"condition\": \"money>=300\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"yiCHangActions\": []\n" +
            "      },\n" +
            "      \"left\": \"325px\",\n" +
            "      \"top\": \"77px\",\n" +
            "      \"ico\": \"iconfont icon-exclude_gateway\",\n" +
            "      \"show\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"node-1566269888000\",\n" +
            "      \"name\": \"普通\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"comm\",\n" +
            "        \"name\": \"普通\",\n" +
            "        \"nodeType\": \"USER_TASK\",\n" +
            "        \"title\": \"副总\",\n" +
            "        \"taskAssignee\": {\n" +
            "          \"activitiId\": \"node-1566269888000\",\n" +
            "          \"activitiName\": \"副总\",\n" +
            "          \"assigneeType\": \"CandidateUsers\",\n" +
            "          \"assigneeVal\": \"1,3\"\n" +
            "        },\n" +
            "        \"filter\": \"comm\",\n" +
            "        \"cls\": \"tool comm\",\n" +
            "        \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "        \"yiCHangActions\": [],\n" +
            "        \"nodeForm\": {\n" +
            "          \"name\": \"表单名称\",\n" +
            "          \"url\": \"表单地址\",\n" +
            "          \"formType\": \"表单类型\",\n" +
            "          \"buttons\": [\n" +
            "            {\n" +
            "              \"name\": \"提交\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"反对\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"驳回\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"其他\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      \"left\": \"448px\",\n" +
            "      \"top\": \"59px\",\n" +
            "      \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "      \"show\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"node-1566269891000\",\n" +
            "      \"name\": \"普通\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"comm\",\n" +
            "        \"name\": \"普通\",\n" +
            "        \"nodeType\": \"USER_TASK\",\n" +
            "        \"title\": \"总经理\",\n" +
            "        \"taskAssignee\": {\n" +
            "          \"activitiId\": \"node-1566269891000\",\n" +
            "          \"activitiName\": \"总经理\",\n" +
            "          \"assigneeType\": \"Assingee\",\n" +
            "          \"assigneeVal\": \"4\"\n" +
            "        },\n" +
            "        \"filter\": \"comm\",\n" +
            "        \"cls\": \"tool comm\",\n" +
            "        \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "        \"yiCHangActions\": [],\n" +
            "        \"nodeForm\": {\n" +
            "          \"name\": \"表单名称\",\n" +
            "          \"url\": \"表单地址\",\n" +
            "          \"formType\": \"表单类型\",\n" +
            "          \"buttons\": [\n" +
            "            {\n" +
            "              \"name\": \"提交\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"反对\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"驳回\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"其他\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      \"left\": \"372px\",\n" +
            "      \"top\": \"302px\",\n" +
            "      \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "      \"show\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"node-1566269893000\",\n" +
            "      \"name\": \"普通\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"comm\",\n" +
            "        \"name\": \"普通\",\n" +
            "        \"nodeType\": \"USER_TASK\",\n" +
            "        \"title\": \"人事\",\n" +
            "        \"taskAssignee\": {\n" +
            "          \"activitiId\": \"node-1566269893000\",\n" +
            "          \"activitiName\": \"人事\",\n" +
            "          \"assigneeType\": \"Assingee\",\n" +
            "          \"assigneeVal\": \"3\"\n" +
            "        },\n" +
            "        \"filter\": \"comm\",\n" +
            "        \"cls\": \"tool comm\",\n" +
            "        \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "        \"yiCHangActions\": [\n" +
            "          {\n" +
            "            \"taskNodeId\": \"node-1566269893000\",\n" +
            "            \"targetNodeId\": \"node-1566269881000\",\n" +
            "            \"filterType\": \"false\",\n" +
            "            \"targetNodeName\": \"部门经理\",\n" +
            "            \"actionType\": \"BOHUI\",\n" +
            "            \"condition\": \"spyj=='驳回部门经理'\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"taskNodeId\": \"node-1566269893000\",\n" +
            "            \"targetNodeId\": \"node-1566269895000\",\n" +
            "            \"filterType\": \"true\",\n" +
            "            \"targetNodeName\": \"结束节点\",\n" +
            "            \"actionType\": \"BOHUI\",\n" +
            "            \"condition\": \"\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"nodeForm\": {\n" +
            "          \"name\": \"表单名称\",\n" +
            "          \"url\": \"表单地址\",\n" +
            "          \"formType\": \"表单类型\",\n" +
            "          \"buttons\": [\n" +
            "            {\n" +
            "              \"name\": \"提交\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"反对\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"驳回\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"其他\",\n" +
            "              \"actionType\": \"动作类型\",\n" +
            "              \"icon\": \"按钮logo\",\n" +
            "              \"conditionScript\": \"条件脚本\",\n" +
            "              \"color\": \"按钮颜色\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      \"left\": \"661px\",\n" +
            "      \"top\": \"201px\",\n" +
            "      \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "      \"show\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"node-1566269895000\",\n" +
            "      \"name\": \"结束\",\n" +
            "      \"info\": {\n" +
            "        \"type\": \"end\",\n" +
            "        \"name\": \"结束\",\n" +
            "        \"nodeType\": \"end\",\n" +
            "        \"title\": \"结束节点\",\n" +
            "        \"filter\": \"end\",\n" +
            "        \"cls\": \"tool end\",\n" +
            "        \"ico\": \"iconfont icon-jieshu\",\n" +
            "        \"yiCHangActions\": []\n" +
            "      },\n" +
            "      \"left\": \"814px\",\n" +
            "      \"top\": \"210px\",\n" +
            "      \"ico\": \"iconfont icon-jieshu\",\n" +
            "      \"show\": true\n" +
            "    }\n" +
            "  ],\n" +
            "  \"lineList\": [\n" +
            "    {\n" +
            "      \"id\": \"line-1566269897000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269880000\",\n" +
            "      \"to\": \"node-1566269881000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269899000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269881000\",\n" +
            "      \"to\": \"node-1566269886000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269900000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269886000\",\n" +
            "      \"to\": \"node-1566269888000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269902000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269886000\",\n" +
            "      \"to\": \"node-1566269891000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269904000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269888000\",\n" +
            "      \"to\": \"node-1566269893000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269906000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269891000\",\n" +
            "      \"to\": \"node-1566269893000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269912000\",\n" +
            "      \"label\": \"驳回部门经理\",\n" +
            "      \"isDefaultExcute\": \"false\",\n" +
            "      \"from\": \"node-1566269893000\",\n" +
            "      \"to\": \"node-1566269881000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"line-1566269946000\",\n" +
            "      \"label\": \"\",\n" +
            "      \"isDefaultExcute\": \"true\",\n" +
            "      \"from\": \"node-1566269893000\",\n" +
            "      \"to\": \"node-1566269895000\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    /**
     * @Description 测试流程保存
     * @Author lr
     * @Date 2019/8/30 14:33
     **/
    @Test
    public void test01() {
        for (int i = 0; i < 5; i++) {
            BpmnModelEntity bpmnModelEntity = JSON.parseObject(jsonStr, BpmnModelEntity.class);
            bpmnModelEntity.setProcessKey("ceshi0");
//            workflowModelService.add(bpmnModelEntity);
        }
    }

    /**
     * @Description 测试流程列表
     * @Author lr
     * @Date 2019/8/30 14:34
     **/
    @Test
    public void test02() {
        TPage<Map> page = new TPage<>();
        Map<String, Object> params = new HashMap<>();
        page.setParams(params);
        TPage<Map> list = workflowModelService.list(page);
        System.out.println(list);
    }

    @Test
    public void test03() {
        String id = "5d809d37131dcc3c404f33df";
        Map<String, Object> result = workflowModelService.getById(id);
        System.out.println(result);
    }

    @Test
    public void test04() {
        String id = "5d80a2f3131dcc6060ec87d9";
        workflowModelService.delById(id);
    }

    @Test
    public void test05() {
        String processKey = "ceshi11";
        TPage<Map> page = new TPage<>();
        page.setParams(new HashMap<String, Object>() {{
            put("processKey", processKey);
        }});
        TPage tPage = workflowModelService.listVersions(page);
        System.out.println(JSON.toJSONString(tPage));
    }

}
