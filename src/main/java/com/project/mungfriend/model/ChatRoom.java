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



    public ChatRoom(Member writer, Member applicant){
        this.roomName = writer.getNickname() + "과 " + applicant.getNickname() + "의 채팅방";
        this.memberList.add(writer);
        this.memberList.add(applicant);
    }
}
