package com.dataseek.xe.util;

import com.dataseek.xe.controller.UserController;
import jdk.internal.dynalink.beans.StaticClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
    protected static Logger logger = LoggerFactory.getLogger(DataUtil.class);

    private static final AtomicInteger atomicInteger = new AtomicInteger(1000);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 校验字符串非空
     * @param inputStr
     * @return
     */
    public static boolean isEmpty(String inputStr) {
        boolean reValue = false;
        if (inputStr == "" || inputStr == null) {
            reValue = true;
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


    public static void sendEmail(String toAddress, String emailMsg) throws AddressException, MessagingException {
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.qq.com");
        //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
        props.put("mail.smtp.port", "587");
        // 此处填写你的账号
        props.put("mail.user", "125863528@qq.com");
        // 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", "");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress from = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(from);

        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress(toAddress);
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject("User Activating");

        // 设置邮件的内容体
        message.setContent(emailMsg, "text/html;charset=UTF-8");

        // 最后当然就是发送邮件啦
        Transport.send(message);

    }

    /**
     * 将date转化为string
     * @param dateVal
     * @return
     */
    public static String transDateToStr(long dateVal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dateVal);
    }

    /**
     * 将date转化为long
     * @param dateStr)
     * @return
     */
    public static long transDateToValue(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateStr).getTime();
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return 0;
        }


    }

}
