package com.project.mungfriend.service;

import com.project.mungfriend.dto.MailResponseDto;
import com.project.mungfriend.util.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailService {

    String subject = "메일 제목입니다.";
    String text = "메일 본문입니다.";

    public MailResponseDto sendMail() {
        MailSender.sendMail("rlafbf1986@gmail.com", subject, text);
        return new MailResponseDto("true", "메일이 전송되었습니다.");
    }
}
