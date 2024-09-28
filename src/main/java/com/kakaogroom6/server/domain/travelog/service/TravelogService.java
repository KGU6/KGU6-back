package com.kakaogroom6.server.domain.travelog.service;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.travelog.dto.res.TravelogSummaryDto;
import com.kakaogroom6.server.domain.travelog.dto.res.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.repository.TravelogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelogService {

    private final TravelogRepository travelogRepository;

    public TravelogsResponseDto getAllTravelogs(String sortBy) {
        List<TravelogEntity> travelogs;
        if ("likes".equalsIgnoreCase(sortBy)) {
            travelogs = travelogRepository.findAllByOrderByLikesDesc();
        } else {
            travelogs = travelogRepository.findAllByOrderByLikesDesc();
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

    public TravelogsResponseDto searchTravelogs(String keyword, String sortBy) {
        List<TravelogEntity> travelogs;
        if ("likes".equalsIgnoreCase(sortBy)) {
            travelogs = travelogRepository.findByTitleContainingOrderByLikesDesc(keyword);
        } else {
            travelogs = travelogRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

    private TravelogSummaryDto convertToSummaryDto(TravelogEntity travelog) {
        return new TravelogSummaryDto(
                travelog.getMember().getName(),
                "", // 메인 이미지 URL (엔티티에 없음)
                "", // 장소 이름 (엔티티에 없음)
                travelog.getTitle(),
                travelog.getCreatedAt()
        );
    }
}
