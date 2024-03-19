package com.sparta.hh2stagram.domain.user.service;

import com.sparta.hh2stagram.domain.user.dto.LoginRequestDto;
import com.sparta.hh2stagram.domain.user.dto.SignupRequestDto;
import com.sparta.hh2stagram.domain.user.dto.UserResponseDto;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.entity.UserRoleEnum;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
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
    * 비밀번호 검증 패턴
    * 비밀번호 조건 최소 8 ~ 최대 15자리
    * 영문 대소문자, 숫자, 특수문자 !@#$%^&*()_~만 허용
    * passwordEncoder 패턴화를 통해 성능 최적화*/
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_~]).{8,15}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /*
    * 회원가입 로직
    * 비밀번호 검증, 암호화 처리
    * email 중복 여부 확인*/
    public void signupUser(SignupRequestDto requestDto) {
        /*비밀번호 검증*/
        String password = requestDto.getPassword();
        if (!pattern.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호는 최소 8자리에서 최대 15자리이며, " +
                    "영어 대소문자(a~zA~Z), 숫자, 특수문자 !@#$%^&*()_~만 사용 가능합니다.");
        }

        /*비밀번호 암호화 처리*/
        password = passwordEncoder.encode(password);

        /*email 중복 검사*/
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        /*유저 권한 부여
        * 관리자 권한 추가 시 권한 검증 메서드 추가 필요*/
        UserRoleEnum role = UserRoleEnum.USER;

        /*DB에 유저 정보 저장*/
        User user = new User(requestDto.getEmail(), requestDto.getPassword(), requestDto.getUsername(), role);

        userRepository.save(user);
    }

    public UserResponseDto loginUser(LoginRequestDto requestDto) {
        return null;
    }
}
