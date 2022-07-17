package com.project.mungfriend.controller;

import com.project.mungfriend.dto.chat.ChatRoomListDto;
import com.project.mungfriend.dto.chat.ChatRoomRequestDto;
import com.project.mungfriend.dto.chat.ChatRoomResponseDto;
import com.project.mungfriend.model.ChatMessage;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.ChatMessageService;
import com.project.mungfriend.service.ChatRoomService;
import com.project.mungfriend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;


    // 채팅방 생성
    @PostMapping("/channel")
    @ResponseBody
    public ChatRoomResponseDto createChatRoom(@RequestBody ChatRoomRequestDto requestDto) {
        log.info("채팅방 생성 requestDto = {}", requestDto);
//        requestDto.setMemberId(SecurityUtil.getCurrentMemberId());
        String username = SecurityUtil.getCurrentMemberUsername();
        log.info("현재 유저의 아이디 = {}", username);

        return chatRoomService.createChatRoom(requestDto);
    }


    // 전체 채팅방 목록 조회
    @GetMapping("/channels")
    @ResponseBody
    public List<ChatRoomListDto> getAllChatRooms() {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberService.getMemberObject(username);

        return chatRoomService.getAllChatRooms(member);
    }


    // 특정 채팅방 조회
    @GetMapping("/channel/{channelId}")
    @ResponseBody
    public ChatRoomResponseDto showChatRoom(@PathVariable Long channelId) {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberService.getMemberObject(username);
        return chatRoomService.showChatRoom(channelId, member);
    }

    //특정 채팅방 삭제
    @DeleteMapping("/channel/{channelId}")
    @ResponseBody
    public boolean deleteChatRoom(@PathVariable Long channelId){
        return chatRoomService.deleteChatRoom(channelId);
    }


    //채팅방 내 메시지 전체 조회
    @GetMapping("/channel/{channelId}/messages")
    @ResponseBody
    public List<ChatMessage> getRoomMessages(@PathVariable Long channelId) {
        return chatMessageService.getMessages(channelId);
    }
}
