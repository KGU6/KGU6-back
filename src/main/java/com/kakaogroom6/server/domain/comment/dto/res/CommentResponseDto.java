package com.kakaogroom6.server.domain.comment.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String userName;
    private String content;
    private String createdAt;
    private String profileImgUrl;
}
