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
            travelogs = travelogRepository.findAllByOrderByLikesDesc()
                    .orElse(List.of());
        } else {
            travelogs = travelogRepository.findAllByOrderByCreatedAtDesc()
                    .orElse(List.of());
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

    public TravelogsResponseDto searchTravelogs(String keyword, String sortBy) {
        List<TravelogEntity> travelogs;
        if ("likes".equalsIgnoreCase(sortBy)) {
            travelogs = travelogRepository.findByTitleContainingOrderByLikesDesc(keyword)
                    .orElse(List.of());
        } else {
            travelogs = travelogRepository.findByTitleContainingOrderByCreatedAtDesc(keyword)
                    .orElse(List.of());
        }

        List<TravelogSummaryDto> travelogSummarys = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new TravelogsResponseDto(travelogSummarys, travelogSummarys.size());
    }

    private TravelogSummaryDto convertToSummaryDto(TravelogEntity travelog) {

        return new TravelogSummaryDto(
                travelog.getMember().getName(),
                travelog.getMainImageUrl(),
                firstPlace != null ? firstPlace.getName() : null,
                travelog.getTitle(),
                travelog.getCreatedAt()
        );
    }
}
