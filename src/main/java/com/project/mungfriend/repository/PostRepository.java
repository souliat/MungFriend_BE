package com.project.mungfriend.repository;

import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
