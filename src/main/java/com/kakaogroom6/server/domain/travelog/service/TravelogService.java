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
import com.kakaogroom6.server.domain.travelog.dto.response.CreateTravelogResponseDTO;
import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.travelog.dto.response.TravelogSummaryDto;
import com.kakaogroom6.server.domain.travelog.dto.response.TravelogsResponseDto;
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
            travelogs = travelogRepository.findByTitleContainingOrderByLikesDesc(location)
                    .orElse(List.of());
        } else {
            travelogs = travelogRepository.findByTitleContainingOrderByCreatedAtDesc(location)
                    .orElse(List.of());
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

    private TravelogSummaryDto convertToSummaryDto(TravelogEntity travelog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = travelog.getCreatedAt().format(formatter);

        return new TravelogSummaryDto(
                travelog.getMember().getName(),
                travelog.getMainImage(),
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

        if (requestDTO.getMainImage() != null) {
            try {
                String mainImageUrl = s3Service.savePhoto(requestDTO.getMainImage(), member.getId());
                travelog.setImageurl(mainImageUrl); // 이미지를 클라우드 타입으로 설정 (필요시 수정)
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload main image", e);
            }
        }

        // 여행기 저장
        TravelogEntity savedTravelog = travelogRepository.save(travelog);

        // 키워드 저장
        saveKeywords(request.getKeywords(), savedTravelog);

        // 장소 저장
        System.out.println("PlaceContent created: " + request.getPlaceContent());
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

}
