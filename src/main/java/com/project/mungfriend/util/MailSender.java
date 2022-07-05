package com.project.mungfriend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

// 회원 가입 시 signup 로직에 해당 static 메서드 호출 필요! (회원가입 축하 메일)
@Component
public class MailSender {

    private static final String user = "mungfriend_official@naver.com";
    private static String password;

    @Value("${spring.mail.password}")
    public void setPassword(String value){
        password = value;
    }

    // 해당 메서드의 파라미터에는 받는 사람의 이메일 주소, 메일 제목, 메일 본문을 넣어준다.
    public static void sendMail(String recieverMailAddr, String subject, String text){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.naver.com");
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.auth", "true");

//        prop.put("mail.smtp.ssl.enable", "true");
//        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            //수신자 메일 주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recieverMailAddr));
            //제목
            message.setSubject(subject);
            //본문
            message.setText(text);
            //메일 보내기
            Transport.send(message);
            System.out.println("메일 전송 완료!");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
