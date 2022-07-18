package com.project.mungfriend.repository;

import com.project.mungfriend.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ChatMessageRepository  {
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, List<ChatMessage>> opsHashChatMessage;
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
    }

    public ChatMessage save(ChatMessage chatMessage) {
        log.info("chatMessage : {}", chatMessage.getMessage());
        log.info("type: {}", chatMessage.getType());
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Long.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));

        Long roomId = chatMessage.getRoomId();
        List<ChatMessage> chatMessageList = opsHashChatMessage.get(CHAT_MESSAGE, roomId);
        if (chatMessageList == null) {
            chatMessageList = new ArrayList<>();
        }
        chatMessageList.add(chatMessage);

        opsHashChatMessage.put(CHAT_MESSAGE, roomId, chatMessageList);

        return chatMessage;
    }

    public List<ChatMessage> findAllMessage(Long roomId) {
        log.info("findAllMessage");
        return opsHashChatMessage.get(CHAT_MESSAGE, roomId);
    }

    public void delete(Long roomId) {
        opsHashChatMessage.delete(CHAT_MESSAGE, roomId);
    }
}
