package com.project.mungfriend.repository;

import com.project.mungfriend.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByRoomId(Long roomId, Pageable pageable);

    List<ChatMessage> findByRoomId(Long roomId);

    void deleteAllByRoomId(Long channelId);
}
