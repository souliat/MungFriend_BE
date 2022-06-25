package com.project.mungfriend.repository;

import com.project.mungfriend.model.DogImageFile;
import com.project.mungfriend.model.ReviewImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageFileRepository extends JpaRepository<ReviewImageFile, Long> {
}
