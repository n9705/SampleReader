package com.example.n9705.samplereader;

/**
 * Created by 86758 on 2018/3/19 0019.
 */


import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailTool {

    public static void sendMail(String str) throws MessagingException, GeneralSecurityException {

        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
//        // 发送服务器需要身份验证
//        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        //开启 SSL 加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        System.out.print("111111");
        Message msg = new MimeMessage(session);
        msg.setSubject("意见反馈");
        msg.setContent(str, "text/html;charset=UTF-8");
        msg.setFrom(new InternetAddress("867588970@qq.com"));

        System.out.print("22222 ");
        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "867588970@qq.com", "oistvkoltuqibbcb");

        transport.sendMessage(msg, new Address[] { new InternetAddress("3136367259@qq.com") });
        transport.close();
    }
}
