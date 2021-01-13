package com.wlznsb.iossupersign.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

/**
 * 使用SMTP协议发送电子邮件
 */
public class MailUtil {

    public static void main(String[] args) throws MessagingException {
        sendText();
    }
    public static void sendText() throws MessagingException {
        //设置发送者的配置信息
        Properties properties = new Properties();
        //发送者的协议
        properties.put("mail.smtp.host","smtp.qq.com");
        properties.put("mail.smtp.auth","true");
        Session session = Session.getInstance(properties);
        //设置调试信息在控制台打印出来
        session.setDebug(true);

        //信息体
        MimeMessage message = new MimeMessage(session);

        //发信者
        Address address = new InternetAddress("2394197228@qq.com");
        message.setFrom(address);
        //收信者
        Address toAddress = new InternetAddress("2524931333@qq.com");


        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送）
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        message.setRecipient(MimeMessage.RecipientType.TO,toAddress);

        //主题
        message.setSubject("米西米西");
        //正文
        message.setText("哈喽啊，饭已ok啦，下来米西吧！！！");
        message.saveChanges();

        //发送邮件
        Transport transport = session.getTransport("smtp");
        //登录
        transport.connect("2394197228@qq.com", "qhazrpcrakwueaij");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();


    }


}
