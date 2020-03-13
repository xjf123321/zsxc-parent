package com.zs.create.config;

import com.zs.create.modules.shiro.authc.ShiroRealm;
import com.zs.create.modules.shiro.authc.aop.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: lingrui
 * @date: 2018/2/7
 * @description: shiro 配置类
 */

@Configuration
public class ShiroConfig {

    /**
     * Filter Chain定义说明
     * <p>
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        //登录接口排除
        filterChainDefinitionMap.put("/sys/login", "anon");
        filterChainDefinitionMap.put("/gen_ts/genTest/list", "anon");
        //获取加密串
        filterChainDefinitionMap.put("/sys/getEncryptedString", "anon");
        //短信验证码
        filterChainDefinitionMap.put("/sys/sms", "anon");
        //手机登录
        filterChainDefinitionMap.put("/sys/phoneLogin", "anon");
        //校验用户是否存在
        filterChainDefinitionMap.put("/sys/user/checkOnlyUser", "anon");
        //用户注册
        filterChainDefinitionMap.put("/sys/user/register", "anon");
        //根据手机号获取用户信息
        filterChainDefinitionMap.put("/sys/user/querySysUser", "anon");
        //用户忘记密码验证手机号
        filterChainDefinitionMap.put("/sys/user/phoneVerification", "anon");
        //用户更改密码
        filterChainDefinitionMap.put("/sys/user/passwordChange", "anon");
        //登录验证码
        filterChainDefinitionMap.put("/auth/2step-code", "anon");
        //图片预览不限制token
        filterChainDefinitionMap.put("/sys/common/view/**", "anon");
        //文件下载不限制token
        filterChainDefinitionMap.put("/sys/common/download/**", "anon");
        filterChainDefinitionMap.put("/sys/common/downloaddoc/**", "anon");
        //pdf预览
        filterChainDefinitionMap.put("/sys/common/pdf/**", "anon");
        //文件上传预览
        filterChainDefinitionMap.put("/sys/common/upload", "anon");
        filterChainDefinitionMap.put("/api/ws/**", "anon");
        filterChainDefinitionMap.put("/ws/asset/**", "anon");
        filterChainDefinitionMap.put("/ws/**", "anon");
        //officePage
        filterChainDefinitionMap.put("/word/**", "anon");
        filterChainDefinitionMap.put("/save", "anon");
        filterChainDefinitionMap.put("/wordNotice", "anon");
        filterChainDefinitionMap.put("/word/pageoffice.js", "anon");
        filterChainDefinitionMap.put("/word/pageoffice.js", "anon");
        filterChainDefinitionMap.put("/poserver.zz", "anon");
        filterChainDefinitionMap.put("/posetup.exe", "anon");
        filterChainDefinitionMap.put("/wordTemplate", "anon");
        filterChainDefinitionMap.put("/noticeTemplate", "anon");
        filterChainDefinitionMap.put("/office/**", "anon");
        filterChainDefinitionMap.put("/loginseal.zz", "anon");
        filterChainDefinitionMap.put("/sealsetup.exe", "anon");
        filterChainDefinitionMap.put("/loginseal.do", "anon");
        filterChainDefinitionMap.put("/poserver.do", "anon");
        filterChainDefinitionMap.put("/adminseal.zz", "anon");

        //pdf预览需要文件
        filterChainDefinitionMap.put("/generic/**", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/csrf", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");

        //性能监控
        filterChainDefinitionMap.put("/actuator/metrics/**", "anon");
        filterChainDefinitionMap.put("/actuator/httptrace/**", "anon");
        filterChainDefinitionMap.put("/actuator/redis/**", "anon");

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>(1);
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        // 未授权界面返回JSON
        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
        shiroFilterFactoryBean.setLoginUrl("/sys/common/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    /**
     * 下面的代码是添加注解支持
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
