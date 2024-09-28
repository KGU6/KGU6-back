package com.kakaogroom6.server.domain.travelog.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTravelogByKewordDTO {
    private String memberName;
    private String title;
    private String imageUrl;
}
