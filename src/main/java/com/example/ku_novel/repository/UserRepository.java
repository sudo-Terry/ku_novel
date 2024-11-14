package com.example.ku_novel.repository;

import com.example.ku_novel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsById(String id);
    boolean existsByNickname(String nickname);

}
