package com.kakaogroom6.server.domain.place.repository;

import com.kakaogroom6.server.domain.place.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
    Optional<PlaceEntity> findFirstByTravelogId(Long travelogId);
    int countByTravelogIdIn(List<Long> travelogIds);
    List<PlaceEntity> findByTravelogId(Long travelogId);
}
