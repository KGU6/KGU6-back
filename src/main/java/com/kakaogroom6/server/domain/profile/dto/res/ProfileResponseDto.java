package com.kakaogroom6.server.domain.profile.dto.res;

import com.kakaogroom6.server.domain.travelog.dto.res.TravelogSummaryDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    /**
     * 프로필 구성 정보
     * 1) 맴버 이름, 프로필 이미지
     * 2) 트래블로그 수, 구름핀 수(트래블로그에 등록된 장소 수 총합)
     * 3) 트래블로그 요약 정보들
     */
    private String memberName;
    private String profileUrl;

    private Integer travelogCount;
    private Integer groomPinCount;

    private List<TravelogSummaryDto> travelogs;
}
