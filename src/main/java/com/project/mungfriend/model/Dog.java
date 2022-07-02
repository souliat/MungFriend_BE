package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.dto.dog.DogProfileRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Dog {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String info = "";

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private boolean isRepresentative;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "dog", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<DogImageFile> dogImageFiles = new ArrayList<>();

    public Dog(DogProfileRequestDto requestDto) {
        this.name = requestDto.getName();
        this.age = requestDto.getAge();
        this.gender = requestDto.getGender();
        this.info = requestDto.getInfo();
        this.size = requestDto.getSize();
        this.isRepresentative = requestDto.getIsRepresentative();
    }

    public void setMember(Member member) {
        this.member = member;
        member.getDogList().add(this);
    }
}
