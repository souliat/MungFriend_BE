package com.project.mungfriend.repository;

import com.project.mungfriend.model.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Long countByPostId(Long PostId);
    Boolean existsByApplicantIdAndPostId(Long memberId, Long postId);

    List<Apply> findAllByApplicantId(Long id);
}
