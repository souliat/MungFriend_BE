package com.project.mungfriend.repository;

import com.project.mungfriend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Boolean existsByPostIdAndGiverId(Long postId, Long giverId);
}
