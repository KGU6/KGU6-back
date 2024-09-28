package com.kakaogroom6.server.domain.comment.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {
    @NotNull(message = "travelogId cannot be null")
    private Long travelogId;

    @NotBlank(message = "content connot be blank")
    private String content;
}