package com.project.mungfriend.repository;

import com.project.mungfriend.model.ChatRoom;
import com.project.mungfriend.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByOrderByCreatedAtDesc();


    List<ChatRoom> findAllByMemberListIsContaining(Member member);
}
