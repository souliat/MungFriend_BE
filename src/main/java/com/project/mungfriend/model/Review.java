package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.dto.review.PostReviewRequestDto;
import com.project.mungfriend.util.Timestamped;
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
public class Review extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private Long postId;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="TAKER_ID")
    private Member taker;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="GIVER_ID")
    private Member giver;

    @OneToMany(mappedBy = "review", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReviewImageFile> reviewImageFile = new ArrayList<>();

    public Review(Member giver, Member taker, PostReviewRequestDto requestDto) {
        this.giver = giver;
        this.taker = taker;

        giver.getGiverReviews().add(this);
        taker.getTakerReviews().add(this);

        this.postId = requestDto.getPostId();
        this.comment = requestDto.getComment();
    }
}
