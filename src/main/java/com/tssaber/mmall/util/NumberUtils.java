package com.tssaber.mmall.util;

import java.text.SimpleDateFormat;

/**
 * @Author:tssaber 生成随机数
 * @Date: 2020/2/3 23:54
 * @Version 1.0
 */
public class NumberUtils {

    public static volatile int Guid = 100;

    public static String getGuid(){
        NumberUtils.Guid += 1;
        long now = System.currentTimeMillis();

        //获取4位的年份
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        //获取时间戳
        String time = dateFormat.format(now);
        String info = now + "";
        int ran = 0;
        if (NumberUtils.Guid > 999){
            NumberUtils.Guid = 100;
        }
        ran = NumberUtils.Guid;

        return (time + info.substring(2,info.length()) + ran);
    }

    public static boolean checkLong(Long orderNo,int length){
        String number = String.valueOf(orderNo);
        return (number.length() == length);
    }
}
