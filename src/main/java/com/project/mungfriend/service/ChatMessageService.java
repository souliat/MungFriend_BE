package com.project.mungfriend.service;

import com.project.mungfriend.model.ChatMessage;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.ChatMessageRepository;
import com.project.mungfriend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;

    // 메시지 전송
    public void sendChatMessage(ChatMessage chatMessage) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
//            chatMessage.setMessage("");
//            chatMessage.setSender("");
            log.info("ENTER 데이터 날라갑니다~!");
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");

            saveNotification(chatMessage);

        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
//            chatMessage.setMessage("");
//            chatMessage.setSender("");
            log.info("QUIT 데이터 날라갑니다~!");
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");

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
