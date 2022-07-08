package com.project.mungfriend.model;

import com.project.mungfriend.dto.chat.ChatRoomRequestDto;
import com.project.mungfriend.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class ChatRoom extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private List<Member> memberList = new ArrayList<>();



    public ChatRoom(ChatRoomRequestDto requestDto, Member writer, Member applicant){
        this.roomName = requestDto.getChannel();
        this.memberList.add(writer);
        this.memberList.add(applicant);
    }
}
