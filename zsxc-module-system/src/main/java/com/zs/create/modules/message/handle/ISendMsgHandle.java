package com.zs.create.modules.message.handle;

/**
 * @Description ISendMsgHandle
 * @Author HeLiu
 * @Date 2019/9/28 9:15
 **/
public interface ISendMsgHandle {

    void sendMsg(String esReceiver, String esTitle, String esContent);
}
