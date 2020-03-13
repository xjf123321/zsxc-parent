package com.zs.create.base.log;

import com.zs.create.modules.system.entity.SysLog;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description 日志的本地队列
 * @Author HeLiu
 * @Date 2019/8/14 11:18
 **/
public final class LogQueue {
    // 队列大小
    public static final int QUEUE_MAX_SIZE = 1000;
    //懒汉模式单例
    private static LogQueue logQueue = new LogQueue();
    // 阻塞队列
    private BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    //私有构造函数
    private LogQueue() {
    }

    //提供统一的入口
    public static LogQueue getInstance() {
        return logQueue;
    }

    /**
     * 消息入队
     *
     * @param sysLog
     * @return
     */
    public boolean push(SysLog sysLog) {
        boolean bol = this.blockingQueue.add(sysLog); // 队列满了就抛出异常，不阻塞
        return bol;

    }

    /**
     * 消息出队
     *
     * @return
     */
    public SysLog poll() {
        SysLog result = null;
        try {
            result = (SysLog) this.blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取队列大小
     *
     * @return
     */
    public int size() {
        return this.blockingQueue.size();
    }

}
