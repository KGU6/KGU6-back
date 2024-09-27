package com.kakaogroom6.server.travelog.dto.res;

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
