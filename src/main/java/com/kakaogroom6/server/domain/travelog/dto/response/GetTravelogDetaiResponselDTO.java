package com.kakaogroom6.server.domain.travelog.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetTravelogDetaiResponselDTO {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<GetOnePlaceResposeDTO> placeContent;
    private List<String> keywords;
    private int likes;
    private String memberName;
    private LocalDateTime createdAt;
}
