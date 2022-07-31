package com.project.mungfriend.repository;

import com.project.mungfriend.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = "member")
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.member LEFT JOIN FETCH p.applyList ORDER BY p.requestStartDate")
    List<Post> findAllByOrderByRequestStartDateByFetchJoin();

    @EntityGraph(attributePaths = "member")
    @Query(value = "SELECT p FROM Post p LEFT JOIN FETCH p.member LEFT JOIN FETCH p.applyList ")
    List<Post> findAllByFetchJoin();

}
