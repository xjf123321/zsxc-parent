package com.zs.create;

import com.alibaba.fastjson.JSON;
import com.zs.create.modules.workflow.assignee.entity.Group;
import com.zs.create.modules.workflow.assignee.entity.GroupUser;
import com.zs.create.modules.workflow.assignee.entity.UserEntity;
import com.zs.create.modules.workflow.assignee.service.impl.AssigneeService;
import com.zs.create.modules.workflow.common.bpmn.BpmnModelHandler;
import com.zs.create.modules.workflow.common.entity.BpmnModelEntity;
import com.zs.create.common.groovy.IGroovyScriptEngine;
import com.zs.create.modules.workflow.deploy.service.impl.ProcessDeployService;
import com.zs.create.modules.workflow.instance.service.impl.ProcessInstanceService;
import com.zs.create.modules.workflow.task.service.IProcessTaskService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: guodl
 * @Date: 2019/8/8 12:10
 * @Description:
 */
@SpringBootTest(classes = {ZsxcSystemApplication.class})
@RunWith(SpringRunner.class)
public class ZsxcSystemApplicationTest {
    @Autowired
    ApplicationContext act;
    @Autowired
    ProcessDeployService deployService;
    @Autowired
    ProcessInstanceService processInstanceService;
    @Autowired
    IProcessTaskService processTaskService;
    @Autowired
    TaskService taskService;
    @Autowired
    AssigneeService assigneeService;

    @Autowired
    IGroovyScriptEngine groovyScriptEngine;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    RepositoryService repositoryService;
    String jsonStr = "\n" +
            "{\n" +
            "    \"name\": \"流程A\",\n" +
            "    \"enableTaskListener\": \"false\",\n" +
            "    \"nodeList\": [\n" +
            "        {\n" +
            "            \"id\": \"node-1565156010000\",\n" +
            "            \"name\": \"节点-1565156010000\",\n" +
            "            \"info\": {\n" +
            "                \"type\": \"start\",\n" +
            "                \"name\": \"开始\",\n" +
            "                \"cls\": \"tool start\",\n" +
            "                \"ico\": \"iconfont icon-start\"\n" +
            "            },\n" +
            "            \"left\": \"45px\",\n" +
            "            \"top\": \"231px\",\n" +
            "            \"ico\": \"iconfont icon-start\",\n" +
            "            \"show\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"node-1565156012000\",\n" +
            "            \"name\": \"节点-1565156012000\",\n" +
            "            \"info\": {\n" +
            "                \"type\": \"user_task\",\n" +
            "                \"name\": \"部门经理审批\",\n" +
            "                \"cls\": \"tool comm\",\n" +
            "                \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "\t\t\t\t\"taskAssignee\":{\n" +
            "\t\t\t\t\t\"activitiId\":\"node-1565156012000\",\n" +
            "\t\t\t\t\t\"activitiName\":\"部门经理审批\",\n" +
            "\t\t\t\t\t\"assigneeType\":\"Assingee\",\n" +
            "\t\t\t\t\t\"assigneeVal\":\"008\"\n" +
            "\t\t\t\t}\n" +
            "            },\n" +
            "\t\t\t\n" +
            "            \"left\": \"187px\",\n" +
            "            \"top\": \"228px\",\n" +
            "            \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "            \"show\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"node-1565160690000\",\n" +
            "            \"name\": \"节点-1565160690000\",\n" +
            "            \"info\": {\n" +
            "                \"type\": \"user_task\",\n" +
            "                \"name\": \"分管副总审批\",\n" +
            "                \"cls\": \"tool comm\",\n" +
            "                \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "\t\t\t\t\"taskAssignee\":{\n" +
            "\t\t\t\t\"activitiId\":\"node-1565160690000\",\n" +
            "\t\t\t\t\"activitiName\":\"分管副总审批\",\n" +
            "\t\t\t\t\"assigneeType\":\"CandidateUsers\",\n" +
            "\t\t\t\t\"assigneeVal\":\"001,002,003\"\n" +
            "\t\t\t\t}\n" +
            "            },\n" +
            "\t\t\t \n" +
            "            \"left\": \"525px\",\n" +
            "            \"top\": \"159px\",\n" +
            "            \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "            \"show\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"node-1565160746000\",\n" +
            "            \"name\": \"节点-1565160746000\",\n" +
            "            \"info\": {\n" +
            "                \"type\": \"user_task\",\n" +
            "                \"name\": \"总经理审批\",\n" +
            "                \"cls\": \"tool comm\",\n" +
            "                \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "\t\t\t\t\"taskAssignee\":{\n" +
            "\t\t\t\t\t\"activitiId\":\"node-1565160746000\",\n" +
            "\t\t\t\t\t\"activitiName\":\"总经理审批\",\n" +
            "\t\t\t\t\t\"assigneeType\":\"CandidateGroups\",\n" +
            "\t\t\t\t\t\"assigneeVal\":\"G_001,G_002,G_003\"\n" +
            "\t\t\t\t}\n" +
            "            },\n" +
            "\t\t\t\n" +
            "            \"left\": \"522px\",\n" +
            "            \"top\": \"265px\",\n" +
            "            \"ico\": \"iconfont icon-yuanjiaojuxing\",\n" +
            "            \"show\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"node-1565160763000\",\n" +
            "            \"name\": \"节点-1565160763000\",\n" +
            "            \"info\": {\n" +
            "                \"type\": \"ExclusiveGateway\",\n" +
            "                \"name\": \"网关\",\n" +
            "                \"cls\": \"tool gate\",\n" +
            "                \"ico\": \"iconfont icon-lingxing\",\n" +
            "\t\t\t\t\"conditions\":[\n" +
            "\t\t\t\t\t{\"gateWayId\":\"node-1565160763000\",\"to\":\"node-1565160690000\",\"condition\":\"days<=3\"},\n" +
            "\t\t\t\t\t{\"gateWayId\":\"node-1565160763000\",\"to\":\"node-1565160746000\",\"condition\":\"days>=3\"}\n" +
            "\t\t\t\t]\n" +
            "            },\n" +
            "\t\t\t \n" +
            "            \"left\": \"375px\",\n" +
            "            \"top\": \"228px\",\n" +
            "            \"ico\": \"iconfont icon-lingxing\",\n" +
            "            \"show\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"node-1565160817000\",\n" +
            "            \"name\": \"节点-1565160817000\",\n" +
            "            \"info\": {\n" +
            "                \"type\": \"end\",\n" +
            "                \"name\": \"结束\",\n" +
            "                \"cls\": \"tool end\",\n" +
            "                \"ico\": \"iconfont icon-jieshu\"\n" +
            "            },\n" +
            "            \"left\": \"755px\",\n" +
            "            \"top\": \"224px\",\n" +
            "            \"ico\": \"iconfont icon-jieshu\",\n" +
            "            \"show\": true\n" +
            "        }\n" +
            "    ],\n" +
            "    \"lineList\": [\n" +
            "        {\n" +
            "            \"id\": \"\",\n" +
            "            \"name\": \"\",\n" +
            "            \"label\": \"\",\n" +
            "            \"from\": \"node-1565156010000\",\n" +
            "            \"to\": \"node-1565156012000\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"\",\n" +
            "            \"name\": \"\",\n" +
            "            \"label\": \"\",\n" +
            "            \"from\": \"node-1565156012000\",\n" +
            "            \"to\": \"node-1565160763000\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"\",\n" +
            "            \"name\": \"\",\n" +
            "            \"label\": \"\",\n" +
            "            \"from\": \"node-1565160763000\",\n" +
            "            \"to\": \"node-1565160690000\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"\",\n" +
            "            \"name\": \"\",\n" +
            "            \"label\": \"\",\n" +
            "            \"from\": \"node-1565160763000\",\n" +
            "            \"to\": \"node-1565160746000\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"\",\n" +
            "            \"name\": \"\",\n" +
            "            \"label\": \"\",\n" +
            "            \"from\": \"node-1565160690000\",\n" +
            "            \"to\": \"node-1565160817000\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"\",\n" +
            "            \"name\": \"\",\n" +
            "            \"label\": \"\",\n" +
            "            \"from\": \"node-1565160746000\",\n" +
            "            \"to\": \"node-1565160817000\"\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";

    @Test
    public void test() {
        String[] beans = act.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println(bean);
        }
        ProcessDeployService deployService = (ProcessDeployService) act.getBean("deployService");
        //  Task task = taskService.createTaskQuery().taskId("001").singleResult();
        System.out.println("任务：" + deployService);
    }

    /**
     * 流程部署测试
     */
    @Test
    public void testDeployService() {
        BpmnModelEntity bpmnModelEntity = JSON.parseObject(jsonStr, BpmnModelEntity.class);
        BpmnModelHandler modelHandler = new BpmnModelHandler("jianting_no", "任务监听流程", bpmnModelEntity);
        Deployment deployment = deployService.invokeWorkFlowDeploy(modelHandler);

    }

    /**
     * 生成图片
     */
    @Test
    public void testgetDeployPic() throws Exception {
        ProcessDefinition jianting = repositoryService.createProcessDefinitionQuery().processDefinitionKey("jianting").singleResult();
        InputStream ins = repositoryService.getResourceAsStream("152501",
                jianting.getResourceName());
        try {

            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            File targetFile = new File("src/main/resources/targetFile.tmp");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 流程启动测试(手动指定任务办理人）
     */
    @Test
    public void testInstanceService1() {
        Map<String, Object> map = new HashMap<>();
        map.put("sqr", "请假2天");
        map.put("days", 2);
        map.put("reason", "请假原因");
        processInstanceService.startProcessInstance("jianting_no", map, false);
    }

    /**
     * 启用任务监听
     */
    @Test
    public void testInstanceService2() {
        Map<String, Object> map = new HashMap<>();
        map.put("sqr", "请假2天");
        map.put("days", 2);
        map.put("reason", "请假原因");
        processInstanceService.startProcessInstance("jianting", map, true);
    }

    /**
     * 测试查询个人任务
     */
    @Test
    public void testqueryMyDaiBanTaskList() {
        String taskAssignee = "001";
        Map<String, Object> map = processTaskService.queryMyDaiBanTaskList(taskAssignee, 0, 10);
        System.out.println(map);
    }

    /**
     * 根据任务id查询任务
     */
    @Test
    public void testGetTask() {
        String taskId = "120008";
        Map<String, Object> map = processTaskService.getTask(taskId);

        Task task = (Task) map.get("task");
        Map<String, Object> val = (Map) map.get("variable");

        System.out.println("================");
    }

    /**
     * 查询多人任务
     */
    @Test
    public void testQueryCandidateUserTaskList() {
        Map<String, Object> map = processTaskService.queryCandidateUserTaskList("003", 0, 10);
        System.out.println(map);
    }

    @Test
    public void testQueryCandidateGroupTaskList() {
        List<String> groupIds = new ArrayList<>();
        groupIds.add("G_001");
        Map<String, Object> map = processTaskService.queryCandidateGroupTaskList(groupIds, 0, 10);
        System.out.println(map);
    }

    @Test
    public void testAddUserAndGroup() {
        UserEntity userEntity1 = new UserEntity("001", "张三");
        assigneeService.addUser(userEntity1);
        UserEntity userEntity2 = new UserEntity("002", "李四");
        assigneeService.addUser(userEntity2);
        UserEntity userEntity3 = new UserEntity("003", "王五");
        assigneeService.addUser(userEntity3);
        Group group1 = new Group("G_001", "总经理角色");
        assigneeService.addGroup(group1);
        Group group2 = new Group("G_002", "副总角色");
        assigneeService.addGroup(group2);
        Group group3 = new Group("G_003", "部门经理角色");
        assigneeService.addGroup(group3);

        GroupUser groupUser1 = new GroupUser("G_001", "001");
        assigneeService.addGroupUser(groupUser1);

        GroupUser groupUser2 = new GroupUser("G_001", "002");
        assigneeService.addGroupUser(groupUser2);

        GroupUser groupUser3 = new GroupUser("G_002", "002");
        assigneeService.addGroupUser(groupUser3);

        GroupUser groupUser4 = new GroupUser("G_003", "001");
        assigneeService.addGroupUser(groupUser4);
    }


    @Test
    public void testClaimTask() {
        processTaskService.claimTask("137504", "002");
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompeleteTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("type", "变量类型170008");
        processTaskService.compeleteTask("170008", variables);
    }

    @Test
    public void testScript() {
        Object obj1 = groovyScriptEngine.executeObject("sysScript.getUserId()", new HashMap<>());
        if (obj1 instanceof String) {
            System.out.println("obj==" + obj1);
        }

        Object obj2 = groovyScriptEngine.executeObject("sysScript.getUserIds()", new HashMap<>());
        if (obj2 instanceof List) {
            System.out.println("obj==" + obj2);
        }

        Object obj3 = groovyScriptEngine.executeObject("sysScript.getCurrentUser()", new HashMap<>());
        if (obj3 instanceof User) {
            System.out.println("obj==" + obj3);
        }

        Object obj4 = groovyScriptEngine.executeObject("sysScript.getGroup()", new HashMap<>());
        if (obj4 instanceof Group) {
            System.out.println("obj==" + obj4);
        }
        String str = "xsss";
        Map<String, Object> map = new HashMap<>();
        map.put("xsss", "xxxxxxx");
        Object obj5 = groovyScriptEngine.executeObject("sysScript.param(" + str + ")", map);
        System.out.println("obj5==" + obj5);
        if (obj5 instanceof String) {
            System.out.println("obj==" + obj5);
        }
    }

    @Test
    public void testCondition() {
        String condition = "days<=3";
        Map<String, Object> map = new HashMap<>();
        map.put("days", 2);
        Boolean bool = groovyScriptEngine.executeBoolean(condition, map);
        System.out.println("==bol==" + bool);
    }

}