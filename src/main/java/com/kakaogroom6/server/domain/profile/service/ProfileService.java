package com.kakaogroom6.server.domain.profile.service;

import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import com.kakaogroom6.server.domain.member.repository.MemberRepository;
import com.kakaogroom6.server.domain.place.entity.PlaceEntity;
import com.kakaogroom6.server.domain.place.repository.PlaceRepository;
import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.profile.dto.res.ProfileResponseDto;
import com.kakaogroom6.server.domain.travelog.dto.res.TravelogSummaryDto;
import com.kakaogroom6.server.domain.travelog.repository.TravelogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository userRepository;
    private final TravelogRepository travelogRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public ProfileResponseDto getProfile(String email) {
        // 맴버 객체 불러오기(맴버 이름, 맴버 프로필 사진 url)
        MemberEntity member = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No Member Found"));

        // 해당 맴버의 트래블로그들 불러오기(해당 맴버의 아이디를 통해, 그 아이디로 등록되어 있는 트래블로그 불러옴)
        List<TravelogEntity> travelogs = travelogRepository.findByMemberId(member.getId())
                .orElse(List.of()); // 빈 리스트 반환

        // 트래블로그 -> 트래블로그 요약으로 형변환
        List<TravelogSummaryDto> travelogSummaries = travelogs.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        int travelogCount = travelogs.size(); // 트래블로그 수
        int groomPinCount = placeRepository.countByTravelogIdIn( // 구름핀 수(트래블로그에 저장된 장소 수를 map을 통해 순회하며 sum)
                travelogs.stream().map(TravelogEntity::getId).collect(Collectors.toList())
        );

        return new ProfileResponseDto(
                member.getName(),
                member.getProfileUrl(),
                travelogCount,
                groomPinCount,
                travelogSummaries
        );
    }

    private TravelogSummaryDto convertToSummaryDto(TravelogEntity travelog) {
        PlaceEntity firstPlace = placeRepository.findFirstByTravelogId(travelog.getId())
                .orElseThrow(null);

        return new TravelogSummaryDto(
                travelog.getMember().getName(),
                firstPlace != null ? firstPlace.getImageUrl() : null, // TODO 대표이미지로 교체
                firstPlace != null ? firstPlace.getName() : null,
                travelog.getTitle(),
                travelog.getCreatedAt()
        );
    }
}