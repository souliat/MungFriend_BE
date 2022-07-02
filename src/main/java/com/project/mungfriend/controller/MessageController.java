package com.project.mungfriend.controller;

import com.project.mungfriend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/api/message")
    public void sendMessage() {
        messageService.sendMessage();
    }
}
