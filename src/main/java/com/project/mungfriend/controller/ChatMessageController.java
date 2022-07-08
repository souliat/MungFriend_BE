package com.project.mungfriend.controller;

import com.project.mungfriend.dto.chat.ChatMessageRequestDto;
import com.project.mungfriend.model.ChatMessage;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.ChatMessageRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.security.jwt.TokenProvider;
import com.project.mungfriend.service.ChatMessageService;
import com.project.mungfriend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @SendTo("/sub/api/chat/rooms/")
    public void message(@RequestBody ChatMessageRequestDto requestDto) {

//        String username = SecurityUtil.getCurrentMemberUsername();
//        Member member = memberRepository.findByUsername(username).orElse(null);

        // 현재 로그인한 member 찾아옴
//        Member member = authService.getMemberInfo();
//        requestDto.setMemberId(member.getId());
//        requestDto.setSender(member.getNickname());
        // 여기가 뭔가 문제가있음.. 편도랑 이야기.
//        requestDto.setMemberId(1L);
//        requestDto.setSender("기천");

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

        // 웹소켓 통신으로 채팅방 토픽 구독자들에게 메시지 보내기
        chatMessageService.sendChatMessage(chatMessage);

        // MySql DB에 채팅 메시지 저장
        chatMessageService.save(chatMessage);
    }

}