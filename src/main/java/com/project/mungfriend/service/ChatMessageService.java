package com.project.mungfriend.service;

import com.project.mungfriend.model.ChatMessage;
import com.project.mungfriend.repository.ChatMessageRepository;
import com.project.mungfriend.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;

    // ws 통신 인증 정보 저장
    public void saveAuthentication(Message<?> message){
        // accessor를 이용하면 내용에 패킷에 접근할 수 있게된다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String jwtToken = accessor.getFirstNativeHeader("token");
        boolean tokenValid = tokenProvider.validateToken(jwtToken);
        // ws 통신으로 올바른 토큰이 왔을 경우 SecurityContextHolder에 저장하는 작업 추가
        if(tokenValid) {
            Authentication authentication = tokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    // 메시지 전송
    public void sendChatMessage(ChatMessage chatMessage) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            log.info("ENTER 데이터 날라갑니다~!");
            chatMessage.setSender("[알림]");
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");

            saveNotification(chatMessage);

        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            log.info("QUIT 데이터 날라갑니다~!");
            chatMessage.setSender("[알림]");
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");

            saveNotification(chatMessage);
        }
        //topic은 chatroom 이다.
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    //알림 저장
    private void saveNotification(ChatMessage chatMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatMessage.getType());
        message.setRoomId(chatMessage.getRoomId());
        message.setSender(chatMessage.getSender());
        message.setMessage(chatMessage.getMessage());
        message.setCreatedAt(chatMessage.getCreatedAt());
        chatMessageRepository.save(message);
    }

    // 메시지 저장
    public void save(ChatMessage chatMessage) {
//        String username = SecurityUtil.getCurrentMemberUsername();
//        Member member = memberService.getMemberObject(username);

        ChatMessage message = new ChatMessage();
        message.setType(chatMessage.getType());
        message.setRoomId(chatMessage.getRoomId());
        message.setMemberId(chatMessage.getMemberId());
        message.setSender(chatMessage.getSender());
        message.setMessage(chatMessage.getMessage());
        message.setCreatedAt(chatMessage.getCreatedAt());
        chatMessageRepository.save(message);
    }

    // destination 정보에서 roomId 추출(String형)
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }


    // 채팅방 내 메시지 전체 조회
    public List<ChatMessage> getMessages(Long channelId) {
        return chatMessageRepository.findByRoomId(channelId);
    }

    // 채팅방 내 메시지 전체 조회(페이지. 무한스크롤 적용)
    public Page<ChatMessage> getChatMessageByRoomId(Long roomId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 150);
        return chatMessageRepository.findByRoomId(roomId, pageable);
    }
}
