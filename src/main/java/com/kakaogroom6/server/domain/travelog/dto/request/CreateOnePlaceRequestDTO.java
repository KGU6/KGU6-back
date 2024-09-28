package com.kakaogroom6.server.domain.travelog.dto.request;

import com.kakaogroom6.server.domain.place.entity.Cloud;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOnePlaceRequestDTO {
    private String placeName;
    private String content;
    private Cloud cloud;
    private double lat;
    private double lng;
    private Long tracelogId;
}
