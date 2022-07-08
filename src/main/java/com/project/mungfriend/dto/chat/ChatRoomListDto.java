package com.project.mungfriend.dto.chat;

import com.project.mungfriend.model.ChatRoom;
import com.project.mungfriend.model.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatRoomListDto {

    private Long id;
    private String channel;
    private List<Member> memberList;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ChatRoomListDto(ChatRoom chatRoom, Member member) {
        this.id = chatRoom.getId();
        this.channel = chatRoom.getRoomName();
        this.memberList = chatRoom.getMemberList();
        this.nickname = member.getNickname();
        this.createdAt = chatRoom.getCreatedAt();
        this.modifiedAt = chatRoom.getModifiedAt();
    }

}
