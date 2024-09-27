package com.kakaogroom6.server.travelog.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelogSummaryDto {
    /**
     * 트래블로그 요약 정보
     * 0) 사용자 이름 -> 검색화면 only
     * 1) 메인 이미지 url
     * 2) 장소 이름
     * 3) 트래블로그 제목
     * 4) 트래블로그 생성일
     */

    private String userName;
    private String imageUrl;
    private String placeName;
    private String title;
    private LocalDateTime createdAt;
}