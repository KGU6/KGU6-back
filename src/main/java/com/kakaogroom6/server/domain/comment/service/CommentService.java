package com.kakaogroom6.server.domain.comment.service;

import com.kakaogroom6.server.domain.comment.dto.req.CommentRequestDto;
import com.kakaogroom6.server.domain.comment.dto.res.CommentResponseDto;
import com.kakaogroom6.server.domain.comment.entity.CommentEntity;
import com.kakaogroom6.server.domain.comment.repository.CommentRepository;
import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import com.kakaogroom6.server.domain.member.repository.MemberRepository;
import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.domain.travelog.repository.TravelogRepository;
import com.kakaogroom6.server.global.errors.code.CommonErrorCode;
import com.kakaogroom6.server.global.errors.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final TravelogRepository travelogRepository;

    public boolean saveComment(String email, CommentRequestDto request){
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.MEMBER_NOT_FOUND));

        TravelogEntity travelog = travelogRepository.findById(request.getTravelogId())
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.TRAVELOG_NOT_FOUND));

        System.out.println("memberinfo" + member.getId() + member.getEmail());

        CommentEntity comment = new CommentEntity(member, travelog, request.getContent());

        commentRepository.save(comment);

        return true;
    }

    public List<CommentResponseDto> getComment(Long travelogId){
        List<CommentEntity> comments = commentRepository.findAllByTravelogId(travelogId)
                .orElse(List.of());

        List<CommentResponseDto> response = comments.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

        return response;
    }

    public CommentResponseDto convertToResponseDto(CommentEntity comment){
        MemberEntity member = memberRepository.findById(comment.getMember().getId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.MEMBER_NOT_FOUND_AT_COMMENT));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = comment.getCreatedAt().format(formatter);

        return new CommentResponseDto(
                comment.getMember().getName(),
                comment.getContent(),
                formattedDate,
                member.getProfileUrl()
        );
    }
}
