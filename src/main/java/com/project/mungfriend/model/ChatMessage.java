package com.project.mungfriend.model;

import com.project.mungfriend.dto.chat.ChatMessageRequestDto;
import com.project.mungfriend.service.MemberService;
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

    // ws socket 통신을 할 경우 HTTP 통신과 달리 SecurityContextHolder 값을 못 불러온다.
    // 현재 JWTFilter가 OncePerRequestFilter를 상속받고 있는데, Authorization Bearer ~ 를 인식하고있다.
    // ws 통신은 OncePerRequestFilter 에 걸리는지 궁금합니다!
    // 따라서 메세지를 작성할 때 (ws 통신)이므로 로그인한 사용자를 불러올 수 없다.
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
