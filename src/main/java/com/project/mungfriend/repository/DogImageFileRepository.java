package com.project.mungfriend.repository;

import com.project.mungfriend.model.DogImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogImageFileRepository extends JpaRepository<DogImageFile, Long> {
    Optional<DogImageFile> findByDogId(long dogId);
}
