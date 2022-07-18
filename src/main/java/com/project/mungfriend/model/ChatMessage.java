package com.project.mungfriend.model;

import com.project.mungfriend.dto.chat.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type;
    private Long roomId;
    private Long memberId;
    private String sender;
    private String message;
    private String createdAt;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", memberId=" + memberId +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    //    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "member_id_joined")
//    private Member member;

    @Builder
    public ChatMessage(MessageType type, Long roomId, String sender, String senderEmail, String senderImg, String message, String createdAt) {
        this.type = type;
        this.roomId = roomId;
//        this.member = null;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.memberId = chatMessageRequestDto.getMemberId();
        this.sender = chatMessageRequestDto.getSender();
        this.message = chatMessageRequestDto.getMessage();
        this.createdAt = chatMessageRequestDto.getCreatedAt();
    }
}
