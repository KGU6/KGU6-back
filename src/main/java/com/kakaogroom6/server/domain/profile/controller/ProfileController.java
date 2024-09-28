package com.kakaogroom6.server.domain.profile.controller;

import com.kakaogroom6.server.domain.profile.dto.res.ProfileResponseDto;
import com.kakaogroom6.server.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileResponseDto getMember(@CookieValue(name = "email", required = true)String email){
        return profileService.getProfile(email);
    }
}
