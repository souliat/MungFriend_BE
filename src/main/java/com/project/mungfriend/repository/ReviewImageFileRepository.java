package com.project.mungfriend.repository;

import com.project.mungfriend.model.ReviewImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageFileRepository extends JpaRepository<ReviewImageFile, Long> {
    List<ReviewImageFile> findAllByReviewId(Long id);
}
