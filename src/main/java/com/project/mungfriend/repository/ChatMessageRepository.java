package com.project.mungfriend.repository;

import com.project.mungfriend.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
        // 역직렬화 !!
        log.info("findAllMessage");
//        byte[] chatMessageList = (byte[]) opsHashChatMessage.get(CHAT_MESSAGE, roomId);

//        redisTemplate.getHashValueSerializer().deserialize();
        return opsHashChatMessage.get(CHAT_MESSAGE, roomId);
    }

    public void delete(Long roomId) {
        opsHashChatMessage.delete(CHAT_MESSAGE, roomId);
    }
}
