package com.project.mungfriend.service;

import com.project.mungfriend.dto.mail.MailResponseDto;
import com.project.mungfriend.util.MailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    String subject = "메일 제목입니다.";
    String text = "메일 본문입니다.";

    public MailResponseDto sendMail() {
        MailSender.sendMail("rlafbf1986@gmail.com", subject, text);
        return new MailResponseDto("true", "메일이 전송되었습니다.");
    }

}
