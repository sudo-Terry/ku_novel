package com.example.ku_novel.service;

import com.example.ku_novel.domain.User;
import com.example.ku_novel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 가입
    public boolean registerUser(String id, String password, String nickname) {
        if (userRepository.existsById(id) || userRepository.existsByNickname(nickname)) {
            return false;
        }
        User user = new User(id, password, nickname);
        userRepository.save(user);
        return true;
    }

    // 아이디 중복 확인
    public boolean isUserIdExists(String id) {
        return userRepository.existsById(id);
    }

    // 닉네임 중복 확인
    public boolean isNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 로그인 시 사용자 확인
    public boolean validateUserCredentials(String id, String password) {
        User user = userRepository.findById(id).orElse(null);
        return user != null && user.getPassword().equals(password);
    }

}