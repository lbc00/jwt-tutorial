package com.example.jwttutorial.controller;

import com.example.jwttutorial.dto.LoginDto;
import com.example.jwttutorial.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticate() throws Exception {
        // given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", "admin");
        paramMap.put("password", "admin");
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsYmMiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjY5MTc4MTU0fQ.yqQD8Utn0uu2ZK-2Q6Ks94tkZd2r8yozGbsrTnPfVPmsP47ib72sZDlK-t3iEOHp8USFYQbH2LlhiBGWA3qMqg";
        // when
        LoginDto loginDto = LoginDto.builder()
                .username("admin")
                .password("admin")
                .build();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // authenticate 호출 될 때, UserDetailsService.loadUserByUsername가 호출된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        given(tokenProvider.createToken(authentication)).willReturn(jwt);
        // then
        mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paramMap))
                )
        //.andExpect(content().string(result))// 응답 본문의 내용을 검증
        ;
    }
}