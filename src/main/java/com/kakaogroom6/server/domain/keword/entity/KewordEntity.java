package com.kakaogroom6.server.domain.keword.entity;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
public class KewordEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelog_id")
    private TravelogEntity travelogEntity;

    public void setDetails(String name, TravelogEntity travelog) {
        this.name = name;
        this.travelogEntity = travelog;
    }
}
