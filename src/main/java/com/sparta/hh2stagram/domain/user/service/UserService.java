package com.sparta.hh2stagram.domain.user.service;

import com.sparta.hh2stagram.domain.user.dto.LoginRequestDto;
import com.sparta.hh2stagram.domain.user.dto.SignupRequestDto;
import com.sparta.hh2stagram.domain.user.dto.UserResponseDto;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.entity.UserRoleEnum;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import com.sparta.hh2stagram.global.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j(topic = "회원가입, 로그인 서비스 로직")
@Service
public class UserService {
    /*
    * 유저 회원가입 로그인 서비스 로직
    * userRepository : DB와 연결
    * JwtUtil : JwtToken 작업
    * passwordEncoder : 비밀번호 암호화
    * 필드 : 생성자 주입 사용
    * */
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    * 회원가입 로직
    * 비밀번호 검증, 암호화 처리
    * email 중복 여부 확인*/
    public void signupUser(SignupRequestDto requestDto) {

        /*비밀번호 암호화 처리*/
        String password = passwordEncoder.encode(requestDto.getPassword());

        String loginId = requestDto.getLoginId();
        // 케이스 나누기(이메일, 전화번호)

        /*email 중복 검사*/
        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getLoginId());
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 또는 전화번호 입니다.");
        }

        /*DB에 유저 정보 저장*/
        User user = new User(requestDto.getLoginId(), requestDto.getLoginId(),requestDto.getPassword(), requestDto.getName(), requestDto.getNickname());

        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomApiException("User not found with id: " + id));
    }

    public UserResponseDto loginUser(LoginRequestDto requestDto) {
        return null;
    }
}
