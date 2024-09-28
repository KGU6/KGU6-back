package com.kakaogroom6.server.domain.travelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaogroom6.server.domain.travelog.dto.request.CreateOnePlaceRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.request.CreateTravelogRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.request.FirstTravelRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.CreateTravelogResponseDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.service.TravelogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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
//    public ResponseEntity<CreateTravelogResponseDTO> createTravelog(
//            @RequestParam("title") String title,
//            @RequestParam("startDate") LocalDate startDate,
//            @RequestParam("endDate") LocalDate endDate,
//            @RequestParam("keywords") List<String> keywords,
//            @RequestParam("email") String email,
//            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
//            @RequestParam("mainPlace") String mainPlace,
//            @RequestPart("placeContent") List<CreateOnePlaceRequestDTO> placeContent) {
//
//        CreateTravelogRequestDTO request = new CreateTravelogRequestDTO();
//        System.out.println("controller"+placeContent);
//        request.setTravelogData(title, startDate, endDate, keywords, email, mainImage, mainPlace, placeContent);
//
//        CreateTravelogResponseDTO response = travelogService.createTravelog(request);
//        return ResponseEntity.ok(response);
//    }
    @PostMapping("/")
    public ResponseEntity<CreateTravelogResponseDTO> createTravelog(
            @RequestPart("mainImage") MultipartFile mainImage,
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


}
