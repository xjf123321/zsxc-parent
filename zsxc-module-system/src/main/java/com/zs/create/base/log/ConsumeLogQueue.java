package com.zs.create.base.log;

import com.zs.create.modules.system.entity.SysLog;
import com.zs.create.modules.system.mapper.SysLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 消费者日志队列 （本地）
 * @Author HeLiu
 * @Date 2019/8/14 11:13
 **/
@Component
public class ConsumeLogQueue {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysLogMapper sysLogMapper;

    @PostConstruct
    public void startrtThread() {
        ExecutorService e = Executors.newFixedThreadPool(2); // 两个大小的固定线程池
        e.submit(new PollLog(sysLogMapper));
        e.submit(new PollLog(sysLogMapper));
    }

    class PollLog implements Runnable {
        SysLogMapper sysLogMapper;

        PollLog(SysLogMapper sysLogMapper) {
            this.sysLogMapper = sysLogMapper;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    SysLog sysLog = LogQueue.getInstance().poll();
                    if (sysLog != null) {
                        Integer res = sysLogMapper.insert(sysLog);
                        logger.info("保存日志【{}】结果：【{}】", sysLog, res);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
