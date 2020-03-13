package com.zs.create.mongodb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

/**
 * 测试mongoDb的使用方法
 * @author lizt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbTest {
	/**
	 * mongoDb的工具类
	 */
	@Autowired
    private MongoTemplate mongoTemploate;
	
	
	@Test
	public void test() {
		MongoCollection<Document> collection =  mongoTemploate.getCollection("student");
		Map<String, Object> results = new HashMap<>();
		Document doc = collection.find().first();
		Object value = null;
		for(String key:doc.keySet()) {
			value = doc.get(key);
			System.out.println(key+"="+value);
			results.put(key, value.toString());
		}
	}
	
	/**
	 * 保存自定义表单信息
	 */
	@Test
	public void test02() throws Exception{
		MongoCollection<Document> collection = mongoTemploate.getCollection("from_config");
		if(collection ==null) {
			collection = mongoTemploate.createCollection("from_config");
		}
		Document document = new Document();
		File formConfig = new File("D:\\config.json");
		String fileContent = new String(FileUtils.readFileToByteArray(formConfig));
		document = document.parse(fileContent);
		collection.insertOne(document);
	}
	
	/**
	 * 获取指定表单配置
	 */
	@Test
	public void test03() {
		MongoCollection<Document> collection = mongoTemploate.getCollection("from_config");
		Bson filter = Filters.eq("_id",new ObjectId("5d49190cd8165f3afc2c3237"));
		Document document = collection.find(filter).first();
		System.out.println(document.toJson());
	}
	
	/**
	 * 根据配置表信息，生成表单内容
	 */
	@Test
	public void test04() {
		
	}
	
	
	

	public static void main(String[] args) throws Exception {
		File formConfig = new File("D:\\config.json");
		String fileContent = new String(FileUtils.readFileToByteArray(formConfig));
		System.out.println(fileContent);
		Document document = new Document();
		document = document.parse(fileContent);
		System.out.println(document);
		
	}
}
