package com.kakaogroom6.server.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @PostMapping("/api/login")
    public ResponseEntity<Void> login(@RequestParam String email, HttpServletResponse response) {
        Cookie cookie = new Cookie("email", email);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");

        response.addCookie(cookie);

        return new ResponseEntity<Void>( HttpStatus.OK );
    }

}
