package com.kakaogroom6.server.domain.travelog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTravelogResponseDTO {
    private Long travelogId;
    private LocalDateTime createdAT;
}
