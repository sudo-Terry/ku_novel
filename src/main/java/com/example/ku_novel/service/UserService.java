package com.example.ku_novel.service;

import com.example.ku_novel.domain.User;
import com.example.ku_novel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

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
        User user = User.builder()
                .id(id)
                .password(password)
                .nickname(nickname)
                .lastAttendance(null)
                .build();

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

    // 사용자 반환
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findNicknameById(String id) {
        return userRepository.findNicknameById(id);
    }

    public User findIdByNickname(String nickname) {
        return userRepository.findIdByNickname(nickname);
    }

    // 포인트가 500 이상인지 확인
    public boolean hasEnoughPoints(String id) {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return false;
            }
            return user.getPoint() >= 500;
        }

    // 포인트 500 차감
    public boolean deductPoints(String id) {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return false;
            }
    
            user.setPoint(user.getPoint() - 500);
            userRepository.save(user); 
            return true;
    }

    public Integer attendanceCheck(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. : " + id));

        LocalDateTime lastAttendance = user.getLastAttendance();
        LocalDateTime today = LocalDateTime.now();

        if (lastAttendance != null && lastAttendance.toLocalDate().isEqual(today.toLocalDate())) {
            throw new IllegalStateException("이미 오늘 출석 체크를 완료했습니다.");
        }

        int points = user.getPoint() + 100;

        user.setPoint(points);
        user.setLastAttendance(today);
        userRepository.save(user);

        return points;
    }

    public void save(User user) {
        userRepository.save(user);
    }
}