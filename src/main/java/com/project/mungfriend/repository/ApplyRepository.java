package com.project.mungfriend.repository;

import com.project.mungfriend.model.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Long countByPostId(Long PostId);
    Boolean existsByApplicantIdAndPostId(Long memberId, Long postId);

    Optional<Apply> findByApplicantIdAndPostId(Long applicantId, Long postId);

    List<Apply> findAllByApplicantId(Long id);
}
