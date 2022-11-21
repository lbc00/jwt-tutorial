package com.example.jwttutorial.dto;

import com.example.jwttutorial.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;
    @NotNull
    @Size(min = 3, max = 100)
    private String password;
    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    private Set<AuthorityDto> authorityDtoSet;

    public static UserInfoDto from(UserInfo user) {
        if(user == null) {
            return null;
        }

        return UserInfoDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
