package com.dataseek.xe.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
    private static final AtomicInteger atomicInteger = new AtomicInteger(1000);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 校验字符串非空
     * @param inputStr
     * @return
     */
    public static boolean isEmpty(String inputStr) {
        boolean reValue = true;
        if (inputStr == "" || inputStr == null) {
            reValue = false;
        }
        return reValue;
    }

    /**
     * email校验，false代表email非法
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean reValue = true;
        if (isEmpty(email)) {
            reValue = false;
        }

        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            reValue = false;
        }
        return  reValue;
    }

    public static String getUserIdByAtomic() {
        atomicInteger.getAndIncrement();
        int cnt = atomicInteger.get();
        String date = simpleDateFormat.format(new Date());
        return date + cnt;
    }

    /**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

}
