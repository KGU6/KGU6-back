package com.kakaogroom6.server.domain.travelog.entity;

import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import com.kakaogroom6.server.domain.place.entity.Cloud;
import com.kakaogroom6.server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "travelog")
public class TravelogEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MemberEntity_id")
    private MemberEntity member;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer likes;
    private String imageurl;

    public void setDetails(String title, LocalDate startDate, LocalDate endDate, MemberEntity member) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }
}
