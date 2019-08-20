package com.dataseek.xe.util;

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

}
