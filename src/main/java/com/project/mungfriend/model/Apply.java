package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.dto.ApplyPostRequestDto;
import com.project.mungfriend.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Apply extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="APPLICANT_ID")
    private Member applicant;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="POST_ID")
    private Post post;

    public Apply(ApplyPostRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }

    public void setMember(Member applicant) {
        this.applicant = applicant;
        applicant.getApplyList().add(this);
    }

    public void setPost(Post post) {
        this.post = post;
        post.getApplyList().add(this);
    }
}
