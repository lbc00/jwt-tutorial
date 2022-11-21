package com.example.jwttutorial.service;

import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.UserInfo;
import com.example.jwttutorial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Component("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(userInfo -> createUser(username, userInfo))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createUser(String username, UserInfo userInfo) {
        log.debug("createUser : "+username);
        log.debug("userinfo : {}", userInfo);

        if(!userInfo.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        /*List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<Authority> authorities = userInfo.getAuthorities();
        for(Authority authority : authorities) {
            log.debug("authority : {}", authority.toString());

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthorityName());
            log.debug("grantedAuthority : {}", grantedAuthority.toString());
            grantedAuthorities.add(grantedAuthority);
        }*/

        List<GrantedAuthority> grantedAuthorities = userInfo.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(userInfo.getUsername(),
                userInfo.getPassword(),
                grantedAuthorities
        );
    }
}
