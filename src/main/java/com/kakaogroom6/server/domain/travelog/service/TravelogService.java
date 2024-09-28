package com.kakaogroom6.server.domain.travelog.service;

import com.kakaogroom6.server.domain.image.service.S3Service;
import com.kakaogroom6.server.domain.keword.entity.KewordEntity;
import com.kakaogroom6.server.domain.keword.repository.KewordRepository;
import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import com.kakaogroom6.server.domain.member.repository.MemberRepository;
import com.kakaogroom6.server.domain.place.entity.PlaceEntity;
import com.kakaogroom6.server.domain.place.repository.PlaceRepository;
import com.kakaogroom6.server.domain.travelog.dto.request.CreateOnePlaceRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.request.CreateTravelogRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.request.FirstTravelRequestDTO;
import com.kakaogroom6.server.domain.travelog.dto.response.*;
import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.travelog.repository.TravelogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.format.DateTimeFormatter;

import java.io.IOException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelogService {

    private final TravelogRepository travelogRepository;
    private final KewordRepository kewordRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public TravelogsResponseDto getAllTravelogs(String sortBy) {
        List<TravelogEntity> travelogs;
        if ("likes".equalsIgnoreCase(sortBy)) {
            travelogs = travelogRepository.findAllByOrderByCreatedAtDesc()
                    .orElse(List.of());
        } else {
            travelogs = travelogRepository.findAllByOrderByLikesDesc()
                    .orElse(List.of());
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

    public TravelogsResponseDto searchTravelogs(String location, String sortBy) {
        List<TravelogEntity> travelogs;
        if ("likes".equalsIgnoreCase(sortBy)) {
            travelogs = travelogRepository.findAllByTitleContainingOrderByLikesDesc(location)
                    .orElse(List.of());
        } else {
            travelogs = travelogRepository.findAllByTitleContainingOrderByCreatedAtDesc(location)
                    .orElse(List.of());
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

//    public List<TravelogSummaryDto>

    private TravelogSummaryDto convertToSummaryDto(TravelogEntity travelog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = travelog.getCreatedAt().format(formatter);

        return new TravelogSummaryDto(
                travelog.getMember().getName(),
                travelog.getImageurl(),
                travelog.getMainPlace(),
                travelog.getTitle(),
                formattedDate
        );
    }


    // 여행기 작성
    public CreateTravelogResponseDTO createTravelog(FirstTravelRequestDTO requestDTO) {

        // 멤버 조회
        MemberEntity member = memberRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        CreateTravelogRequestDTO request = requestDTO.getTravelogRequest();

        // 여행기 엔티티 생성
        TravelogEntity travelog = new TravelogEntity();
        travelog.setDetails(request.getTitle(), request.getStartDate(), request.getEndDate(), member);

        System.out.println("image status"+requestDTO.getMainImage());

        // 메인 이미지 설정
        if (requestDTO.getMainImage() != null && !requestDTO.getMainImage().isEmpty()) {
            try {
                String mainImageUrl = s3Service.savePhoto(requestDTO.getMainImage(), member.getId());
                travelog.setImageurl(mainImageUrl); // 업로드된 이미지 URL 저장
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload main image", e);
            }
        } else {
            // 기본 이미지 설정 (S3의 기본 이미지 URL)
            String defaultImageUrl = "kgu6.c36yuu8wcket.ap-northeast-2.rds.amazonaws.com 3306 root fb83ec45-a36c-44b8-e1df-ac4068c76ac2";
            travelog.setImageurl(defaultImageUrl); // 기본 이미지 URL 저장
        }

        // 여행기 저장
        TravelogEntity savedTravelog = travelogRepository.save(travelog);

        // 키워드 저장
        saveKeywords(request.getKeywords(), savedTravelog);

        // 장소 저장
        savePlaces(request.getPlaceContent(), savedTravelog);

        return CreateTravelogResponseDTO.builder()
                .travelogId(savedTravelog.getId())
                .createdAT(savedTravelog.getCreatedAt())
                .build();
    }

    private void saveKeywords(List<String> keywords, TravelogEntity travelog) {
        if (keywords != null) {
            for (String keyword : keywords) {
                KewordEntity keword = new KewordEntity();
                keword.setDetails(keyword, travelog);
                kewordRepository.save(keword);
            }
        }
    }

    private void savePlaces(List<CreateOnePlaceRequestDTO> places, TravelogEntity travelog) {
        if (places != null) {
            for (CreateOnePlaceRequestDTO place : places) {
                PlaceEntity placeEntity = new PlaceEntity();
                placeEntity.setDetails(
                        place.getPlaceName(),
                        place.getContent(),
                        place.getCloud(),
                        place.getLat(),
                        place.getLng(),
                        travelog
                );
                placeRepository.save(placeEntity);
            }
        }
    }


    // 여행기 상세 조회
    public GetTravelogDetaiResponselDTO getTravelogDetail(Long travelogId) {
        TravelogEntity travelog = travelogRepository.findById(travelogId)
                .orElseThrow(() -> new IllegalArgumentException("Travelog not found"));

        GetTravelogDetaiResponselDTO responseDTO = new GetTravelogDetaiResponselDTO();
        responseDTO.setTitle(travelog.getTitle());
        responseDTO.setStartDate(travelog.getStartDate());
        responseDTO.setEndDate(travelog.getEndDate());
        responseDTO.setMemberName(travelog.getMember().getName());
        responseDTO.setCreatedAt(travelog.getCreatedAt());

        // 키워드 조회
        List<String> keywords = getTravelogKeywords(travelogId);
        responseDTO.setKeywords(keywords);

        // 장소 정보를 가져오기
        List<GetOnePlaceResposeDTO> placeContent = placeRepository.findByTravelogId(travelogId)
                .stream()
                .map(this::convertPlaceToDTO)
                .collect(Collectors.toList());
        responseDTO.setPlaceContent(placeContent);

        responseDTO.setLikes(travelog.getLikes() == null ? 0 : travelog.getLikes());

        return responseDTO;
    }

    // 장소 DTO 변환 메소드
    private GetOnePlaceResposeDTO convertPlaceToDTO(PlaceEntity place) {
        GetOnePlaceResposeDTO dto = new GetOnePlaceResposeDTO();
        dto.setPlaceName(place.getName());
        dto.setContent(place.getContent());
        dto.setCloud(place.getCloud());
        dto.setLat(place.getLat());
        dto.setLng(place.getLng());
        return dto;
    }

    // 키워드 변환 메소드
    private List<String> getTravelogKeywords(Long travelogId) {
        List<KewordEntity> keywords = kewordRepository.findByTravelogEntityId(travelogId);
        return keywords.stream()
                .map(KewordEntity::getName) // 키워드 엔티티에서 이름만 가져오기
                .collect(Collectors.toList());
    }


    // 키워드별 여행기 조회
    public List<GetTravelogByKewordDTO> getTravelogsByKeyword(String keyword) {
        List<TravelogEntity> travelogs = travelogRepository.findByKeyword(keyword);
        return travelogs.stream().map(this::toDto).collect(Collectors.toList());
    }

    private GetTravelogByKewordDTO toDto(TravelogEntity travelogEntity) {
        GetTravelogByKewordDTO dto = new GetTravelogByKewordDTO();
        dto.setMemberName(travelogEntity.getMember().getName());
        dto.setTitle(travelogEntity.getTitle());
        dto.setImageUrl(travelogEntity.getImageurl());
        return dto;
    }
}
