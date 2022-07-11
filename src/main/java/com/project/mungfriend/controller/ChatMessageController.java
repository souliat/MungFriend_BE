package com.project.mungfriend.controller;

import com.project.mungfriend.dto.chat.ChatMessageRequestDto;
import com.project.mungfriend.model.ChatMessage;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.ChatMessageService;
import com.project.mungfriend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 채팅 메시지를 @MessageMapping 형태로 받는다
    // 웹소켓으로 publish 된 메시지를 받는 곳이다
    // 메시지 보내고 DB에 채팅 메시지 저장하기.
    @MessageMapping("/api/chat/message")
//    @SendTo("/sub/api/chat/rooms/")
    public void message(@RequestBody ChatMessageRequestDto requestDto, Message<?> message) {

        // ws 통신에 담겨온 토큰 값으로 인증 정보 저장하기.
//        chatMessageService.saveAuthentication(message);

//        String username = SecurityUtil.getCurrentMemberUsername();
//        Member member = memberRepository.findByUsername(username).orElse(null);

        // 메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        requestDto.setCreatedAt(dateResult);

        // DTO 로 채팅 메시지 객체 생성
//        assert member != null;
        ChatMessage chatMessage = new ChatMessage(requestDto);

        // MySql DB에 채팅 메시지 저장
        chatMessageService.save(chatMessage);

        // 웹소켓 통신으로 채팅방 토픽 구독자들에게 메시지 보내기
        chatMessageService.sendChatMessage(chatMessage);


    }

}
