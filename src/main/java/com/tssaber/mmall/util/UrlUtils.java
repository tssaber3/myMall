package com.tssaber.mmall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/22 0022 23:57
 */
public class UrlUtils {

    private static final Logger log = LoggerFactory.getLogger(UrlUtils.class);

    /**
     * url加密
     * @param  url
     * @return url
     */
    public static String getKuaishouSign(String url){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((url).getBytes("UTF-8"));
            byte[] b = md5.digest();

            int i;
            StringBuilder buf = new StringBuilder();
            for (int offset = 0;offset < b.length;offset++){
                i = b[offset];
                if (i < 0){
                    i += 256;
                }
                if (i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            url = buf.toString();
            System.out.println("result = " + url);
        } catch (Exception e) {
            log.error("error",e);
        }
        return url;
    }

    public static void main(String[] args) {
        String str = "http://tssaber.top";
        str = UrlUtils.getKuaishouSign(str);
        System.out.println(str);
    }
}
