package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class DogImageFile {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String imageUrl;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="DOG_ID")
    private Dog dog;

    public DogImageFile(String imageUrl){
        this.imageUrl = imageUrl;
    }

    // 연관관계 편의 메소드 작성해야할듯. Dog <-> DogImageFile
    public void setDog(Dog dog) {
        this.dog = dog;
        dog.getDogImageFiles().add(this);
    }
}
