package com.project.mungfriend.repository;

import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Long countByPostId(Long PostId);
    Boolean existsByApplicantIdAndPostId(Long memberId, Long postId);
}
