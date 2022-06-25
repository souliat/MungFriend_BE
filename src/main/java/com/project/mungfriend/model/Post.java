package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.enumeration.Category;
import com.project.mungfriend.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String dogProfileIds;

    @Column(nullable = false)
    private Long applyCount = 0L;

    @Column(nullable = false)
    private boolean applyByMe = false;

    @Column(nullable = false)
    private boolean isComplete = false;

    @Column(nullable = false)
    private LocalDateTime requestStartDate;

    @Column(nullable = false)
    private LocalDateTime requestEndDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Apply> applies = new ArrayList<>();

}
