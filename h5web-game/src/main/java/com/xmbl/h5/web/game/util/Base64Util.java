package com.xmbl.h5.web.game.util;


import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile 2018-12-24 -- 18:16
 * @Version 1.0
 * @Description
 */
public class Base64Util {

    /**
     * 将 s 进行 BASE64 编码
     *
     * @return String
     */
    public static String encode(String s) {
        if (s == null)
            return null;
        String res = "";
        try {
            res = new String(Base64.getEncoder().encode(s.getBytes("GBK")));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     *
     * @date 2015-3-4 上午09:24:26
     */
    public static String decode(String s) {
        if (s == null)
            return null;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] b = decoder.decode(s);
            return new String(b, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return void
     * @author lifq
     * @date 2015-3-4 上午09:23:17
     */
    public static void main(String[] args) {
        System.out.println(Base64Util.encode("哈哈"));
        System.out.println(Base64Util.decode("uf65/g=="));

    }

}
