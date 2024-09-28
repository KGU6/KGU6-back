package com.kakaogroom6.server.domain.travelog.controller;

import com.kakaogroom6.server.domain.travelog.dto.res.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.service.TravelogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/travelog")
public class TravelogController {

    private final TravelogService travelogService;

    @GetMapping
    public ResponseEntity<TravelogsResponseDto> getAllTravelogs(
            @RequestParam(defaultValue = "latest") String sortBy){
        TravelogsResponseDto response = travelogService.getAllTravelogs(sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<TravelogsResponseDto> searchTravelogs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "latest") String sortBy ){
        TravelogsResponseDto response = travelogService.searchTravelogs(keyword, sortBy);
        return ResponseEntity.ok(response);
    }
}
