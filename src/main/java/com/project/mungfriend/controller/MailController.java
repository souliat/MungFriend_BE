package com.project.mungfriend.controller;

import com.project.mungfriend.dto.mail.MailResponseDto;
import com.project.mungfriend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    // 메일 전송 API
    @PostMapping("/api/mails")
    public MailResponseDto sendMail(){
        return mailService.sendMail();
    }
}
