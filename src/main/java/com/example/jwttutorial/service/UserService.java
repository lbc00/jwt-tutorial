package com.example.jwttutorial.service;

import com.example.jwttutorial.dto.UserInfoDto;
import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.UserInfo;
import com.example.jwttutorial.exception.DuplicateMemberException;
import com.example.jwttutorial.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserInfoDto signup(UserInfoDto userInfoDto) {
        if(userRepository.findOneWithAuthoritiesByUsername(userInfoDto.getUsername()).orElse(null) != null){
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        UserInfo userInfo = UserInfo.builder()
                .username(userInfoDto.getUsername())
                .password(passwordEncoder.encode(userInfoDto.getPassword()))
                .nickname(userInfoDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserInfoDto.from(userRepository.save(userInfo));
    }
}
