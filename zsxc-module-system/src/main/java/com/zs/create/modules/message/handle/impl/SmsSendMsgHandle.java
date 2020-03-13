package com.zs.create.modules.message.handle.impl;


import com.zs.create.modules.message.handle.ISendMsgHandle;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description SmsSendMsgHandle
 * @Author HeLiu
 * @Date 2019/9/28 9:15
 **/
@Slf4j
public class SmsSendMsgHandle implements ISendMsgHandle {

    @Override
    public void sendMsg(String esReceiver, String esTitle, String esContent) {
        // TODO Auto-generated method stub
        log.info("发短信");
    }

}
