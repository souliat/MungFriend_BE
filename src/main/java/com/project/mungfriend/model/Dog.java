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
    private Boolean isRepresentative;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    //1. cascade = CascadeType.REMOVE
    // -> 부모 객체 삭제 시 연관관계로 맺어진 자식 객체가 먼저 삭제된다. (CascadeDelete)
    //2. cascade = CascadeType.PERSIST, orphanRemoval = true
    // -> 부모 객체가 삭제되어 고아가 되거나
    // -> 부모 객체와의 연관 관계를 끊은 뒤 (dog.getDogImageFiles().remove(0))
    // 영속성을 전이했을 경우 (DB에 부모 변경 사항 저장 @Transactional을 붙이거나 dogRepository.save()가 발생) 자식 객체도 삭제된다.
    @OneToMany(mappedBy = "dog", cascade = CascadeType.REMOVE)
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
