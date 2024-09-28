package com.kakaogroom6.server.domain.member.entity;

import com.kakaogroom6.server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "member")
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(name = "profile_url", length = 255)
    private String profileUrl;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String password;
}
