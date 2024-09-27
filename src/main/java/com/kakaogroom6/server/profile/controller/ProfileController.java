package com.kakaogroom6.server.profile.controller;

import com.kakaogroom6.server.profile.dto.res.ProfileResponseDto;
import com.kakaogroom6.server.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{name}")
    public ProfileResponseDto getMember(@PathVariable String name){
        return profileService.getProfile(name);
    }
}
