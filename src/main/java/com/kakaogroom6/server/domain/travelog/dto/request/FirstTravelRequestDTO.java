package com.kakaogroom6.server.domain.travelog.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FirstTravelRequestDTO {

    private String email;
    private MultipartFile mainImage;
    private CreateTravelogRequestDTO travelogRequest;
}
