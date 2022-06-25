package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name="APPLICANT_ID")
    private Member applicant;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="WRITER_ID")
    private Member writer;

    @OneToMany(mappedBy = "review")
    private List<ReviewImageFile> reviewImageFile = new ArrayList<>();
}
