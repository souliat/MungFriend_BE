package com.project.mungfriend.model;

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
    private String sender;

    @Column
    private String message;

    @Column
    private String createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @Builder
    public ChatMessage(MessageType type, Long roomId, String sender, String senderEmail, String senderImg, String message, String createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.member = null;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

//    @Builder
//    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, AuthService authService) {
//        this.type = chatMessageRequestDto.getType();
//        this.roomId = chatMessageRequestDto.getRoomId();
////        this.user = authService.getMyInfo(chatMessageRequestDto.getMemberId());
////        this.member = authService.getMemberInfo();
//        // authservice 확인좀....해봐야겠다... 편도랑 이야기함.
//        this.memberId = chatMessageRequestDto.getMemberId();
//        this.sender = chatMessageRequestDto.getSender();
//        this.message = chatMessageRequestDto.getMessage();
//        this.createdAt = chatMessageRequestDto.getCreatedAt();
//    }
}
