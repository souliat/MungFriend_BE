package com.project.mungfriend.service;

import com.project.mungfriend.util.MessageSender;

import java.util.Random;

public class PhoneCheckService {
    public static String sendRandomMessage(String phoneNum) {
//        PhoneCheck message = new PhoneCheck();
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        System.out.println("회원가입 문자 인증 => " + numStr);

        MessageSender.sendSMS(phoneNum, numStr);


        return numStr;
    }

}
