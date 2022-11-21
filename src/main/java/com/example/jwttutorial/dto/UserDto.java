package com.example.jwttutorial.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {
    @NotNull
    @Size(min = 3, max = 50)
    private String usename;
    @NotNull
    @Size(min = 3, max = 100)
    private String password;
    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;
}
