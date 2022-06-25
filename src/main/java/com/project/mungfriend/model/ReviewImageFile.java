package com.project.mungfriend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ReviewImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String imageUrl;

    @ManyToOne
    @JoinColumn(name="REVIEW_ID")
    private Review review;

//    public Imagefile(String imagefile, Post post){
//        this.imagefile = imagefile;
//        this.post = post;
//    }
}
