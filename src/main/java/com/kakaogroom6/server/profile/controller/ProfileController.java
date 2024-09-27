package com.kakaogroom6.server.profile.controller;

import com.kakaogroom6.server.profile.dto.res.ProfileResponseDto;
import com.kakaogroom6.server.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
