package com.kakaogroom6.server.domain.travelog.controller;

import com.kakaogroom6.server.domain.travelog.dto.res.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.service.TravelogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 여행기 등록
//    @PostMapping("/")
//    public ResponseEntity<CreateTravelogResponseDTO> createtravelog(@RequestBody CreateTravelogRequestDTO request) {
//        CreateTravelogResponseDTO response = travelogService.createTravelog(request);
//        return ResponseEntity.ok(response);
//    }
}
