package com.zs.create.form;

import com.alibaba.fastjson.JSONObject;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.zs.create.modules.form.entity.DataBaseAttribute;
import com.zs.create.modules.form.entity.FormControls;
import com.zs.create.modules.form.entity.OnlineForm;
import com.zs.create.modules.form.entity.PageAttribute;
import com.zs.create.modules.form.service.IOnlineFormService;
import com.zs.create.modules.system.entity.DocRecord;
import com.zs.create.modules.system.entity.ZsCarGuocheng;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.mapper.DocRecordMapper;
import com.zs.create.modules.system.mapper.ZsCarGuochengMapper;
import com.zs.create.modules.system.mapper.ZsDocRecordMapper;
import com.zs.create.vo.TPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FormTest {

    @Autowired
    private IOnlineFormService onlineFormService;
    @Autowired
    private ZsDocRecordMapper zsDocRecordMapper;
    @Autowired
    private DocRecordMapper docRecordMapper;
    @Autowired
    private ZsCarGuochengMapper zsCarGuochengMapper;

    /**
     * @Description 测试online表单保存
     * @Author HeLiu
     * @Date 2019/8/30 14:33
     **/
    @Test
    public void test01() {
        OnlineForm onlineForm = new OnlineForm();

        //页面属性
        PageAttribute pageAttribute1 = new PageAttribute();
        pageAttribute1.setFieldName("userName");
        pageAttribute1.setFieldDesc("用户名");
        pageAttribute1.setControlLength(32);
        pageAttribute1.setControlType("input");
        PageAttribute pageAttribute2 = new PageAttribute();
        pageAttribute2.setFieldName("age");
        pageAttribute2.setFieldDesc("年龄");
        pageAttribute2.setControlLength(2);
        pageAttribute2.setControlType("number");

        //数据库属性
        DataBaseAttribute dataBaseAttribute1 = new DataBaseAttribute();
        dataBaseAttribute1.setFieldName("userName");
        dataBaseAttribute1.setFieldDesc("用户名");
        dataBaseAttribute1.setFieldLength(32);
        dataBaseAttribute1.setFieldType("String");
        DataBaseAttribute dataBaseAttribute2 = new DataBaseAttribute();
        dataBaseAttribute2.setFieldName("age");
        dataBaseAttribute2.setFieldDesc("年龄");
        dataBaseAttribute2.setFieldLength(2);
        dataBaseAttribute2.setFieldType("Integer");

        List<PageAttribute> pageAttributeList = new ArrayList<>();
        pageAttributeList.add(pageAttribute1);
        pageAttributeList.add(pageAttribute2);
        List<DataBaseAttribute> dataBaseAttributeArrayList = new ArrayList<>();
        dataBaseAttributeArrayList.add(dataBaseAttribute1);
        dataBaseAttributeArrayList.add(dataBaseAttribute2);

        onlineForm.setTableName("hl_form_test");
        onlineForm.setTableDesc("何流表单测试");
        onlineForm.setPageAttribute(pageAttributeList);
        onlineForm.setDataBaseAttribute(dataBaseAttributeArrayList);

        onlineFormService.add(onlineForm);

    }

    /**
     * @Description 测试online表单列表
     * @Author HeLiu
     * @Date 2019/8/30 14:34
     **/
    @Test
    public void test02() {
        TPage<Map<String, Object>> page = new TPage<>();
        Map<String, Object> params = new HashMap<String, Object>();
        page.setParams(params);
        TPage<Map<String, Object>> list = onlineFormService.list(page);
        System.out.println(list);
    }

    @Test
    public void test03() {
        String id = "";
        Map<String, Object> result = onlineFormService.getById(id);
        System.out.println(result);
    }

    @Test
    public void test04() {
        String id = "";
        onlineFormService.delById(id);
    }

    @Test
    public void test05() {
        OnlineForm onlineForm = new OnlineForm();

        //页面属性
        PageAttribute pageAttribute1 = new PageAttribute();
        pageAttribute1.setFieldName("userName");
        pageAttribute1.setFieldDesc("用户名");
        pageAttribute1.setControlLength(32);
        pageAttribute1.setControlType("input");
        PageAttribute pageAttribute2 = new PageAttribute();
        pageAttribute2.setFieldName("age");
        pageAttribute2.setFieldDesc("年龄");
        pageAttribute2.setControlLength(2);
        pageAttribute2.setControlType("number");
        PageAttribute pageAttribute3 = new PageAttribute();
        pageAttribute3.setFieldName("address");
        pageAttribute3.setFieldDesc("地址");
        pageAttribute3.setControlLength(64);
        pageAttribute3.setControlType("input");

        //数据库属性
        DataBaseAttribute dataBaseAttribute1 = new DataBaseAttribute();
        dataBaseAttribute1.setFieldName("userName");
        dataBaseAttribute1.setFieldDesc("用户名");
        dataBaseAttribute1.setFieldLength(32);
        dataBaseAttribute1.setFieldType("String");
        DataBaseAttribute dataBaseAttribute2 = new DataBaseAttribute();
        dataBaseAttribute2.setFieldName("age");
        dataBaseAttribute2.setFieldDesc("年龄");
        dataBaseAttribute2.setFieldLength(2);
        dataBaseAttribute2.setFieldType("Integer");
        DataBaseAttribute dataBaseAttribute3 = new DataBaseAttribute();
        dataBaseAttribute3.setFieldName("address");
        dataBaseAttribute3.setFieldDesc("地址");
        dataBaseAttribute3.setFieldLength(64);
        dataBaseAttribute3.setFieldType("String");

        List<PageAttribute> pageAttributeList = new ArrayList<>();
        pageAttributeList.add(pageAttribute1);
        pageAttributeList.add(pageAttribute2);
        List<DataBaseAttribute> dataBaseAttributeArrayList = new ArrayList<>();
        dataBaseAttributeArrayList.add(dataBaseAttribute1);
        dataBaseAttributeArrayList.add(dataBaseAttribute2);

        onlineForm.setTableName("hl_form_test");
        onlineForm.setTableDesc("何流表单测试编辑测试");
        onlineForm.setPageAttribute(pageAttributeList);
        onlineForm.setDataBaseAttribute(dataBaseAttributeArrayList);

        onlineFormService.edit(onlineForm);
    }

    @Test
    public void test06() {
        List<FormControls> result = onlineFormService.getFormControlsData("hl_form_test111");
        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void test07() {
        TPage<Map> page = new TPage<>();
        Map<String, Object> params = new HashMap<String, Object>();
        page.setParams(params);
        System.out.println(JSONObject.toJSONString(onlineFormService.listByGroup(page)));
    }

    @Test
    public static void main(String[] args) {
        File inputWord = new File("C:/Users/20772/Desktop/123.docx");
        File outputFile = new File("C:/Users/20772/Desktop/123qwe.pdf");
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
