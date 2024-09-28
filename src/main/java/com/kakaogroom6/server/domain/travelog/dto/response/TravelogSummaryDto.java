package com.kakaogroom6.server.domain.travelog.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelogSummaryDto {
    private String userName;
    private String imageUrl;
    private String placeName;
    private String title;
    private String createdAt;
}