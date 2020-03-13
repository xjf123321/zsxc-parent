package com.zs.create.config;

import com.zhuozhengsoft.pageoffice.poserver.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;



import java.io.IOException;

/**
 * @Description :初始化Bean
 * ---------------------------------
 */
@Configuration
public class BeanLinitConf {

    // PageOffice配置
    @Value(value = "${posyspath}")
    private String posyspath;

    /***
     * PageOffice 注册
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws IOException {
        Server poserver = new Server();
        //设置PageOffice注册成功后,license.lic文件存放的目录
        //String path = ResourceUtils.getURL("classpath:").getPath();
        Resource resource = new ClassPathResource("");
        String property = System.getProperty("java.class.path");
        //System.out.println("resource:" + resource.getFile().getAbsolutePath() + "----------------" + "path" + path );
        poserver.setSysPath(posyspath);
        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
        // 下面是把资源文件暴露出来，必须配置，否则页面访问不了
        srb.addUrlMappings("/poserver.zz", "/posetup.exe", "/pageoffice.js", "/jquery.min.js", "/pobstyle.css", "/sealsetup.exe");
        return srb;
    }

    public void setPosyspath(String posyspath) {
        this.posyspath = posyspath;
    }

}
