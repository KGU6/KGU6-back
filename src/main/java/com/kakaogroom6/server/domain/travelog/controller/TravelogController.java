package com.kakaogroom6.server.domain.travelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaogroom6.server.domain.travelog.dto.request.CreateTravelogRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.request.FirstTravelRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.CreateTravelogResponseDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.GetTravelogDetaiResponselDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.service.TravelogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/")
    public ResponseEntity<CreateTravelogResponseDTO> createTravelog(
            @RequestPart(value = "mainImage") MultipartFile mainImage,
            @RequestPart("travelog") String travelogJson,
            @RequestParam("email") String email
    ) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // LocalDate 처리 가능

        // travelogJson 파싱
        CreateTravelogRequestDTO request;
        try {
            request = objectMapper.readValue(travelogJson, CreateTravelogRequestDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid JSON format for travelog data.");
        }

        // FirstTravelRequestDTO 생성 및 이메일과 이미지 설정
        FirstTravelRequestDTO requestDTO = new FirstTravelRequestDTO();
        requestDTO.setMainImage(mainImage);
        requestDTO.setEmail(email);
        requestDTO.setTravelogRequest(request);

        CreateTravelogResponseDTO response = travelogService.createTravelog(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{travelogId}")
    public ResponseEntity<GetTravelogDetaiResponselDTO> getTravelogDetail(@PathVariable Long travelogId) {
        GetTravelogDetaiResponselDTO travelogDetail = travelogService.getTravelogDetail(travelogId);
        return ResponseEntity.ok(travelogDetail);
    }
}
