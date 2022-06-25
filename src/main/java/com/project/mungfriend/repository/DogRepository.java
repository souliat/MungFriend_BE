package com.project.mungfriend.repository;

import com.project.mungfriend.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
