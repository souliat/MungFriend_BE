package com.project.mungfriend.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MailSenderApache {
    private static final String user = "mungfriend_official@naver.com";
    private static String password;

    @Value("${spring.mail.password}")
    public void setPassword(String value){
        password = value;
    }

    public static void sendMail(String recieverMailAddr, String subject, String text){

        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.naver.com");
        email.setSmtpPort(465);
        email.setAuthentication("mungfriend_official", password);

        email.setSSLOnConnect(true);
        email.setStartTLSEnabled(true);

        try {
            email.setFrom(user, "멍친구", "utf-8");
            email.addTo(recieverMailAddr, "", "utf-8");
            email.setSubject(subject);
            email.setMsg(text);

            email.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }

    }
}
