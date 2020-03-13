package com.zs.create.modules.message.handle.impl;

import com.zs.create.modules.message.handle.ISendMsgHandle;
import lombok.extern.slf4j.Slf4j;

/**
 *@Description WxSendMsgHandle
 *@Author HeLiu
 *@Date 2019/9/28 9:15
 **/
@Slf4j
public class WxSendMsgHandle implements ISendMsgHandle {

    @Override
    public void sendMsg(String esReceiver, String esTitle, String esContent) {
        // TODO Auto-generated method stub
        log.info("发微信消息模板");
    }

}
