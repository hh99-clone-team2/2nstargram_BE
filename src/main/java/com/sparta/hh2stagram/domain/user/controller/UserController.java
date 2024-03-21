package com.sparta.hh2stagram.domain.user.controller;

import com.sparta.hh2stagram.domain.user.dto.SignupRequestDto;
import com.sparta.hh2stagram.domain.user.dto.UserResponseDto;
import com.sparta.hh2stagram.domain.user.dto.LoginRequestDto;
import com.sparta.hh2stagram.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserController", description = "회원 가입, 로그인 API 컨트롤러")
@Slf4j(topic = "회원 가입, 로그인")
@RestController
@RequestMapping("/api/user")
public class UserController {

    /*
    * UserService 필드 주입(생성자 사용)*/
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "이메일 또는 휴대폰 번호, 비밀번호, 유저 이름을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입 완료")
    /*회원가입 기능 호출*/
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDto requestDto) {

        userService.signupUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 이메일 또는 휴대폰 번호, 비밀번호를 입력해 로그인할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "로그인 완료")
    /*로그인 기능 호출*/
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(requestDto, response));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<UserResponseDto>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUsername(username));
    }
}
