package com.zs.create.mysql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zs.create.modules.system.entity.SysUser;
import com.zs.create.modules.system.service.ISysUserService;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlTest {
	@Autowired
	private ISysUserService userService;
	/**
	 * 測試枚舉類型在MySQL-plus中的應用
	 */
	@Test
	public void test() {
		SysUser user =  userService.getById("e9ca23d68d884d4ebb19d07889727dae");
		System.out.println(user);
	}
	
	@Test
	public void test02() {
		SysUser user = userService.getUserByName("admin");
		System.out.println(user);
	}

}
