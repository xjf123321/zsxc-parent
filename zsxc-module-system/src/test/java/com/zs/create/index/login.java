package com.zs.create.index;

import com.zs.create.base.util.AesEncryptUtil;
import com.zs.create.common.api.vo.Result;
import com.zs.create.modules.system.entity.ZsBasicCar;
import com.zs.create.modules.system.entity.ZsMeettingRecord;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.mapper.ZsDocRecordMapper;
import com.zs.create.modules.system.mapper.ZsSqLeaveMapper;
import com.zs.create.modules.system.service.ZsBasicCarService;
import com.zs.create.modules.system.service.ZsMeettingRecordService;
import com.zs.create.modules.system.service.ZsSqCarService;
import com.zs.create.modules.system.service.ZsSqRoomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class login {
    @Autowired
    private ZsBasicCarService zsBasicCarService;

    @Autowired
    private ZsMeettingRecordService zsMeettingRecordService;

    @Autowired
    private ZsDocRecordMapper zsDocRecordMapper;

    @Autowired
    private ZsSqRoomService zsSqRoomService;

    @Autowired
    private ZsSqCarService zsSqCarService;

    @Autowired
    private ZsSqLeaveMapper zsSqLeaveMapper;


    /*@Test
    public void test01() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        map = zsBasicCarService.status();
        System.out.println("0000000000000000000");
        System.out.println(map);
    }*/

    @Test
    public void test02(){
        String id = "31d4e969ba98346931832795431c5716";
        ZsMeettingRecord zsMeettingRecord = zsMeettingRecordService.selectById(id);
        System.out.println("0000000000000"+zsMeettingRecord);
    }

    @Test
    public void test03(){
        Result<ZsMeettingRecord> result = new Result<ZsMeettingRecord>();
        ZsMeettingRecord zsMeettingRecord = new ZsMeettingRecord();
        zsMeettingRecord.setId("bdd2e728f50cf52e4d9bbdfd539e27f0");
        zsMeettingRecord.setMeettingId("d5c625e63d9b6ade578c918b9c66c393");
        zsMeettingRecord.setMeetingMinutes("真好");
        zsMeettingRecord.setUrl("jsdlkfjls");
        String id = zsMeettingRecord.getId();
        System.out.println(id);
        if (id == null){
            try {
                zsMeettingRecordService.add(zsMeettingRecord);
                result.success("添加成功");
            }catch (Exception e){
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }else{
            try {
                zsMeettingRecordService.updateById(zsMeettingRecord);
                result.success("修改成功");
            }catch (Exception e){
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
    }

   /* @Test
    public void test04(){
        Long a = zsDocRecordMapper.count("22222");
        System.out.println(a);
    }*/

    public int workDays(String strStartDate, String strEndDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        try {
            cl1.setTime(df.parse(strStartDate));
            cl2.setTime(df.parse(strEndDate));

        } catch (ParseException e) {
            System.out.println("日期格式非法");
            e.printStackTrace();
        }

        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            if (cl1.get(Calendar.DAY_OF_WEEK) != 7 && cl1.get(Calendar.DAY_OF_WEEK) != 1)
                count++;
            cl1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return count;

    }

    @Test
    public void test001(){
        String strStartDate = "2019-11-13";
        String strEndDate = "2019-11-16";
        int count = workDays(strStartDate,strEndDate);
        System.out.println(count);
    }

    /*
    * 修改会议室状态和会议结束时间
    * */
    @Test
    public void test002(){
        String meettingRoomName = "会议室二";
        String id = "7c8180b3ded319e9ff4375887d7d6f7c";
        zsSqRoomService.updateMeettingStatusAndMeettingEnd(meettingRoomName,id);
    }

    /*
    * 会议室修改状态
    * */
    /*@Test
    public void test003(){
        String meettingRoomName = "会议室二";
        zsSqRoomService.updateMeettingStatus(meettingRoomName);
    } */

    /*
    * 车辆修改状态
    * */
    @Test
    public void test004(){
        String plateNumber = "wj001,wj002,wj003";
        zsSqCarService.updateCarStatus(plateNumber);
    }

    /*
    * 车辆出场修改状态和归场时间
    * */
    /*@Test
    public void test005(){
        String plateNumber = "wj001,wj002,wj003";
        String id = "04bb4bcd705393a19be7d9ee7386c91f";
        zsSqCarService.updateCarStatusAndInDate(plateNumber,id);
    }*/

    @Test
    public void test006(){
        String aa = "11111";
        String[] arr = aa.split(",");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    /*@Test
    public void test008(){
        String a1 = "2019-12-10 09:19:01";
        String a2 = "2019-12-12 10:19:01";
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 =  formatter.parse(a1);
            Date date2 = formatter.parse(a2);
            List<ZsBasicCar> carList = zsBasicCarService.listCars(date1,date2);
            System.out.println(carList.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void test009(){
        String aa = "3e9cc472ce4ff7ce";
        try {
            String password = AesEncryptUtil.desEncrypt(aa.replaceAll("%2B", "\\+")).trim();
            System.out.println(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
