package com.kakaogroom6.server.domain.profile.service;

import com.kakaogroom6.server.domain.image.service.S3Service;
import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import com.kakaogroom6.server.domain.member.repository.MemberRepository;
import com.kakaogroom6.server.domain.place.repository.PlaceRepository;
import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.profile.dto.res.ProfileResponseDto;
import com.kakaogroom6.server.domain.travelog.dto.response.TravelogSummaryDto;
import com.kakaogroom6.server.domain.travelog.repository.TravelogRepository;
import com.kakaogroom6.server.global.errors.code.CommonErrorCode;
import com.kakaogroom6.server.global.errors.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final TravelogRepository  travelogRepository;
    private final PlaceRepository placeRepository;
    private final S3Service s3Service;

    @Transactional
    public ProfileResponseDto getProfile(String email) {
        // 맴버 객체 불러오기(맴버 이름, 맴버 프로필 사진 url)
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.MEMBER_NOT_FOUND));

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

//    public boolean uploadProfile(String email, MultipartFile profileImage) throws IOException {
//
//        MemberEntity member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new RestApiException(CommonErrorCode.MEMBER_NOT_FOUND));
//
//        try{
//            String profileImageUrl = s3Service.saveProfileImg(profileImage, member.getId());
//            member.setProfileUrl(profileImageUrl);
//            memberRepository.save(member);
//        }catch(IOException e){
//            throw new RestApiException(CommonErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
//        }
//
//        return true;
//    }

    private TravelogSummaryDto convertToSummaryDto(TravelogEntity travelog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = travelog.getCreatedAt().format(formatter);

        return new TravelogSummaryDto(
                travelog.getMember().getName(),
                travelog.getMainImage(),
                travelog.getMainPlace(),
                travelog.getTitle(),
                formattedDate
        );
    }
}