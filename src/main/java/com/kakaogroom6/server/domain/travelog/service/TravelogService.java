package com.kakaogroom6.server.domain.travelog.service;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.travelog.dto.res.TravelogSummaryDto;
import com.kakaogroom6.server.domain.travelog.dto.res.TravelogsResponseDto;
import com.kakaogroom6.server.domain.travelog.repository.TravelogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelogService {

    private final TravelogRepository travelogRepository;

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
}
