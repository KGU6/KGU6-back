package com.kakaogroom6.server.domain.place.entity;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import com.kakaogroom6.server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "place")
public class PlaceEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TravelogEntity_id")
    private TravelogEntity travelog;

    private String name;
    @Lob
    private String content;
    private Cloud cloud;
    private double lat;
    private double lng;

    public void setDetails(String name, String content, Cloud cloud, double latitude, double longitude, TravelogEntity travelog) {
        this.name = name;
        this.content = content;
        this.cloud = cloud;
        this.lat = latitude;
        this.lng = longitude;
        this.travelog = travelog;
    }
}
