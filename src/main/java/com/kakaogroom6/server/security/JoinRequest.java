package com.kakaogroom6.server.security;

import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디는 필수항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임은 필수항목입니다.")
    private String name;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .build();
    }

    public MemberEntity toEntity(String encodedPassword) {
        return MemberEntity.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .build();
    }

}
