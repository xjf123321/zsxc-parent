package com.zs.create.modules.monitor.service.impl;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 功能说明:自定义邮件检测
 *
 * @author: lingrui
 * @email: lingrui@163.com
 * @date: 2019-06-29
 */
@Component
public class MailHealthIndicator implements HealthIndicator {


    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    int check() {
        //可以实现自定义的数据库检测逻辑
        return 0;
    }
}
