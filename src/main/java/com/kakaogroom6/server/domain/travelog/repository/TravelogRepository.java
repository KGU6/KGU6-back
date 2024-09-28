package com.kakaogroom6.server.domain.travelog.repository;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelogRepository extends JpaRepository<TravelogEntity, Long> {
    Optional<List<TravelogEntity>> findByMemberId(Long memberId);

    List<TravelogEntity> findAllByOrderByCreatedAtDesc();
    List<TravelogEntity> findAllByOrderByLikesDesc();
    List<TravelogEntity> findByTitleContainingOrderByCreatedAtDesc(String keyword);
    List<TravelogEntity> findByTitleContainingOrderByLikesDesc(String keyword);


}
