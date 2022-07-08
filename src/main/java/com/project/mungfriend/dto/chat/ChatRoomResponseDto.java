package com.project.mungfriend.dto.chat;

import com.project.mungfriend.model.ChatRoom;
import com.project.mungfriend.model.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ChatRoomResponseDto {

    private Long id;
    private String channel;
    private Member member;


    public ChatRoomResponseDto(ChatRoom chatRoom, Member writer) {
        this.id = chatRoom.getId();
        this.channel = chatRoom.getRoomName();
        this.member = writer;
    }
}
