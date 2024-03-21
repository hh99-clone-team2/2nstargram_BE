package com.sparta.hh2stagram.domain.user.service;

import com.sparta.hh2stagram.domain.user.dto.LoginRequestDto;
import com.sparta.hh2stagram.domain.user.dto.SignupRequestDto;
import com.sparta.hh2stagram.domain.user.dto.UserResponseDto;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import com.sparta.hh2stagram.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException("User not found with username: " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomApiException("User not found with id: " + id));
    }

    /*
     * 회원가입 로직
     * 비밀번호 검증, 암호화 처리
     * email 중복 여부 확인*/
    @Transactional
    public Map<String, String> signupUser(SignupRequestDto requestDto) {
        String email = null;
        String phoneNumber = null;

        /*비밀번호 암호화 처리*/
        String password = passwordEncoder.encode(requestDto.getPassword());

        String loginId = requestDto.getLoginId();
        // 케이스 나누기(이메일, 전화번호)
        if (loginId.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            email = loginId;

            /*email 중복 검사*/
            Optional<User> checkEmail = userRepository.findByEmail(email);
            if (checkEmail.isPresent()) {
                throw new IllegalArgumentException("중복된 Email 입니다.");
            }
        }

        if (loginId.matches("^\\d{11}$")) {
            phoneNumber = loginId;

            /*phone number 중복 검사*/
            Optional<User> checkPhone = userRepository.findByPhoneNumber(phoneNumber);
            if (checkPhone.isPresent()) {
                throw new IllegalArgumentException("중복된 전화번호 입니다.");
            }
        }

        if (email == null && phoneNumber == null) {
            throw new IllegalArgumentException("잘못된 형식의 이메일이거나 전화번호 입니다.");
        }

        /*닉네임 중복 검사*/
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent()) {
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

        return Map.of("msg", "회원가입이 완료되었습니다.");
    }

    /*
     * 로그인 로직*/
    @Transactional
    public UserResponseDto loginUser(LoginRequestDto requestDto, HttpServletResponse response) {
        String loginId = requestDto.getLoginId();
        String password = requestDto.getPassword();

        // login id 일치하는 유저 정보 가져오기
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

        return new UserResponseDto(user.getUsername(), user.getName());
    }

    /*
     * 사용자 이름으로 유저 검색*/
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUserByUsername(String username) {
        List<User> userList = userRepository.findByUsernameContains(username);

        return userList.stream().map(UserResponseDto::new).toList();
    }

    /*
     * 로그인 정보에서 유저 찾기*/
    private User getUser(String loginId) {
        // 이메일인 경우
        if (loginId.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            return userRepository.findByEmail(loginId).orElseThrow(
                    () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
            );
        }

        // 전화번호인 경우
        if (loginId.matches("^\\d{11}$")) {
            return userRepository.findByPhoneNumber(loginId).orElseThrow(
                    () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
            );
        }

        // 사용자 이름인 경우
        Optional<User> user = userRepository.findByUsername(loginId);
        if (user.isPresent()) {
            return user.get();
        }

        throw new IllegalArgumentException("아이디, 비밀번호를 확인해주세요.");
    }
}
