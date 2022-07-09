package com.project.mungfriend.model;

import com.project.mungfriend.dto.chat.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private MessageType type;

    @Column
    private Long roomId;

    @Column
    private Long memberId;

    @Column
    private String sender;

    @Column
    private String message;

    @Column
    private String createdAt;

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
