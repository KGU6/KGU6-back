package com.kakaogroom6.server.domain.travelog.repository;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelogRepository extends JpaRepository<TravelogEntity, Long> {
    Optional<List<TravelogEntity>> findByMemberId(Long memberId);
    Optional<List<TravelogEntity>> findAllByOrderByCreatedAtDesc();
    Optional<List<TravelogEntity>> findAllByOrderByLikesDesc();

    // TODO JPA Title -> 장소 프로퍼티명 반영해야함!!
    Optional<List<TravelogEntity>> findByTitleContainingOrderByCreatedAtDesc(String location);
    Optional<List<TravelogEntity>> findByTitleContainingOrderByLikesDesc(String location);
}
