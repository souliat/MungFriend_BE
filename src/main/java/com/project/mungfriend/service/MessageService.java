package com.project.mungfriend.service;


import com.project.mungfriend.util.MessageSender;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public void sendMessage() {
        MessageSender.sendSMS();
    }
}
