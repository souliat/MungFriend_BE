package com.project.mungfriend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
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
        // ssl protocols 설정 해줘야함
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.tls.enable", "true");

//        prop.put("mail.smtp.ssl.enable", "true");
//        prop.put("mail.smtp.ssl.trust", "smtp.naver.com");

        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));

            //수신자 메일 주소
            System.out.println("recieverMailAddr = " + recieverMailAddr);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recieverMailAddr));
            //제목
            message.setSubject(subject);

            // 메일 HTML 로 전송하기 -> 테스트 필요!!!
            // 메일에 출력할 HTML 추가 -> 미리 만들어둬야함!!!
            StringBuffer sb = new StringBuffer();
            sb.append("<h3>[멍친구]</h3>\n");
            sb.append("<h4>").append(text).append("</h4>\n");
            String html = sb.toString();

            //본문 HTML, TEXT
            Multipart mParts = new MimeMultipart();
            MimeBodyPart mTextPart = new MimeBodyPart();
            MimeBodyPart mFilePart = null;

            mTextPart.setText(html, "UTF-8", "html");
            mParts.addBodyPart(mTextPart);
            message.setContent(mParts);
//            message.setText(text);

            // MIME 타입 설정
            MailcapCommandMap mailCapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mailCapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mailCapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mailCapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mailCapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mailCapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mailCapCmdMap);

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
