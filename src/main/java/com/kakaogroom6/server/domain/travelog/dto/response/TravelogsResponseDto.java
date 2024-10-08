package com.kakaogroom6.server.domain.travelog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelogsResponseDto {
    private List<TravelogSummaryDto> travelogs;
    private Integer length;
}
