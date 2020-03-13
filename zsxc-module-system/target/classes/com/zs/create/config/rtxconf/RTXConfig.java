package com.zs.create.config.rtxconf;

import rtx.RTXSvrApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class RTXConfig {

    /*
    * 消息提醒
    * */

    public static void sendNotify(String receivers, String title, String msg, String type, String delayTime) {

        int iRet = -1;
        RTXSvrApi rtxSvrapiObj = new RTXSvrApi();
        if (rtxSvrapiObj.Init())
        {
            /*rtxSvrapiObj.setServerIP("192.168.20.88");
            rtxSvrapiObj.setServerPort(6000);*/
            String serverIP = rtxSvrapiObj.getServerIP();
            System.out.println(serverIP+"========================");

            iRet = rtxSvrapiObj.sendNotify(receivers, title, msg, type, delayTime);
            if (iRet == 0)
            {
                System.out.println("发送成功");

            }
            else
            {
                System.out.println("发送失败");
            }

        }
        rtxSvrapiObj.UnInit();

    }

    public static void sendNotify(String receivers, String title, String msg) {

        String sendImg = "/SendNotify.cgi?";                          // RTX发送消息接口
        StringBuffer sendMsgParams = new StringBuffer(sendImg);

        int tag = 1;

        sendMsgParams.append("&receiver=" + receivers);
        try {

            sendMsgParams.append("&msg=" +  URLEncoder.encode(msg, "utf-8"));
            sendMsgParams.append("&title=" + URLEncoder.encode(title, "utf-8"));

            URL url = new URL("HTTP", "192.168.20.88", 8012, sendMsgParams.toString());
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("======sendnotifyCgi========" + line);
                //tag=Integer.parseInt(line);
            }
            in.close();

            httpconn.disconnect();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            tag = 0;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            tag = 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            tag = 0;
        }

    }


}
