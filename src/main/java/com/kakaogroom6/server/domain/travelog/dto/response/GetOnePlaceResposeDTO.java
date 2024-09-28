package com.kakaogroom6.server.domain.travelog.dto.response;

import com.kakaogroom6.server.domain.place.entity.Cloud;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOnePlaceResposeDTO {
    private String placeName;
    private String content;
    private Cloud cloud;
    private double lat;
    private double lng;
}
