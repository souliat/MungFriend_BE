package com.project.mungfriend.repository;

import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.DogImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogImageFileRepository extends JpaRepository<DogImageFile, Long> {
}
