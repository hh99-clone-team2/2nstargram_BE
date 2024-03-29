package com.sparta.hh2stagram.global.security;

import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);

        return new UserDetailsImpl(user);
    }

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
