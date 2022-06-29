package com.project.mungfriend.repository;

import com.project.mungfriend.model.MyPageIntroduceOnly;
import com.project.mungfriend.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

<<<<<<< HEAD

=======
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);
>>>>>>> c0c03476a9c0d65ceeba084ff767a76b10c6be0b
}
