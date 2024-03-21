package com.sparta.hh2stagram.domain.user.service;

import com.sparta.hh2stagram.domain.user.dto.LoginRequestDto;
import com.sparta.hh2stagram.domain.user.dto.SignupRequestDto;
import com.sparta.hh2stagram.domain.user.dto.UserResponseDto;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        String email = "";
        String phoneNumber = "";

        /*비밀번호 암호화 처리*/
        String password = passwordEncoder.encode(requestDto.getPassword());

        String loginId = requestDto.getLoginId();
        // 케이스 나누기(이메일, 전화번호)
        if (loginId.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            email = loginId;
            phoneNumber = null;

            /*email 중복 검사*/
            Optional<User> checkEmail = userRepository.findByEmail(email);
            if (checkEmail.isPresent()) {
                throw new IllegalArgumentException("중복된 Email 입니다.");
            }
        }

        if (loginId.matches("^\\d{11}$")) {
            phoneNumber = loginId;
            email = null;

            /*phone number 중복 검사*/
            Optional<User> checkPhone = userRepository.findByPhoneNumber(phoneNumber);
            if (checkPhone.isPresent()) {
                throw new IllegalArgumentException("중복된 전화번호 입니다.");
            }
        }

        if (email.isBlank() && phoneNumber.isBlank()) {
            throw new IllegalArgumentException("잘못된 형식의 이메일이거나 전화번호 입니다.");
        }

        /*닉네임 중복 검사*/
        Optional<User> checkNickname = userRepository.findByUsername(requestDto.getUsername());
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("등록된 닉네임 입니다.");
        }

        /*DB에 유저 정보 저장*/
        User user = User.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .username(requestDto.getUsername())
                .name(requestDto.getName())
                .password(password)
                .build();

        userRepository.save(user);
    }

    public UserResponseDto loginUser(LoginRequestDto requestDto, HttpServletResponse response) {
        String loginId = requestDto.getLoginId();
        String password = requestDto.getPassword();

        User user = getUser(loginId);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtUtil.createAccessToken(loginId, user.getRole());

        // HTTP 응답 헤더에 JWT 토큰 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // 로그인 성공 메시지를 로그에 출력
        log.info("사용자 '{}'의 로그인 성공", user.getUsername());

        return new UserResponseDto(user.getUsername());
    }

    private User getUser(String loginId) {
        // 케이스 나누기(이메일, 전화번호)
        if (loginId.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            return userRepository.findByEmail(loginId).orElseThrow(
                    () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
            );
        }

        if (loginId.matches("^\\d{11}$")) {
            return userRepository.findByPhoneNumber(loginId).orElseThrow(
                    () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
            );
        }
        throw new IllegalArgumentException("아이디, 비밀번호를 확인해주세요.");
    }
}
