package com.kakaogroom6.server.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String loginId;
    private String password;

}
