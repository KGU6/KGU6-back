package com.kakaogroom6.server.domain.travelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaogroom6.server.domain.comment.dto.req.CommentRequestDto;
import com.kakaogroom6.server.domain.comment.dto.res.CommentResponseDto;
import com.kakaogroom6.server.domain.comment.service.CommentService;
import com.kakaogroom6.server.domain.travelog.dto.request.CreateTravelogRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.request.FirstTravelRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.CreateTravelogResponseDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.GetTravelogByKewordDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.GetTravelogDetaiResponselDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.service.TravelogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/travelog")
public class TravelogController {

    private final TravelogService travelogService;
    private final CommentService commentService;

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
            @RequestPart(value = "mainImage",required = false) MultipartFile mainImage,
            @RequestPart("travelog") String travelogJson,
            @Value("${security.email}") String email
    ) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // travelogJson 파싱
        CreateTravelogRequestDTO request;
        try {
            request = objectMapper.readValue(travelogJson, CreateTravelogRequestDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid JSON format for travelog data.");
        }

        FirstTravelRequestDTO requestDTO = new FirstTravelRequestDTO();
        requestDTO.setMainImage(mainImage);
        requestDTO.setEmail(email);
        requestDTO.setTravelogRequest(request);

        CreateTravelogResponseDTO response = travelogService.createTravelog(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(
            @Value("${security.email}")String email,
            @Valid @RequestBody CommentRequestDto request){
        boolean response = commentService.saveComment(email, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comment/{travelogId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable Long travelogId){
        List<CommentResponseDto> response = commentService.getComment(travelogId);
        return ResponseEntity.ok(response);
    }

    //여행기 상세 조회
    @GetMapping("/{travelogId}")
    public ResponseEntity<GetTravelogDetaiResponselDTO> getTravelogDetail(@PathVariable Long travelogId) {
        GetTravelogDetaiResponselDTO travelogDetail = travelogService.getTravelogDetail(travelogId);
        return ResponseEntity.ok(travelogDetail);
    }

    // 키워드별 여행기 조회
    @GetMapping("/keyword")
    public ResponseEntity<List<GetTravelogByKewordDTO>> getTravelogByKeyword(@RequestParam String keyword) {
        List<GetTravelogByKewordDTO> travelogs = travelogService.getTravelogsByKeyword(keyword);
        return ResponseEntity.ok(travelogs);
    }

}
