package com.kakaogroom6.server.domain.travelog.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateTravelogRequestDTO {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CreateOnePlaceRequestDTO> placeContent;
    private List<String> keywords;
    private String mainPlace;
}
