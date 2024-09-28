package com.kakaogroom6.server.domain.travelog.repository;

import com.kakaogroom6.server.domain.travelog.entity.TravelogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelogRepository extends JpaRepository<TravelogEntity, Long> {
    Optional<List<TravelogEntity>> findByMemberId(Long memberId);
    Optional<List<TravelogEntity>> findAllByOrderByCreatedAtDesc();
    Optional<List<TravelogEntity>> findAllByOrderByLikesDesc();


    Optional<List<TravelogEntity>> findAllByTitleContainingOrderByCreatedAtDesc(String location);
    Optional<List<TravelogEntity>> findAllByTitleContainingOrderByLikesDesc(String location);

    @Query("SELECT t FROM TravelogEntity t WHERE t.id IN " +
            "(SELECT k.travelogEntity.id FROM KewordEntity k WHERE k.name LIKE %:keyword%)")
    List<TravelogEntity> findByKeyword(@Param("keyword") String keyword);


    Optional<List<TravelogEntity>> findAllByMainPlaceContainingOrderByCreatedAtDesc(String mainPlace);
    Optional<List<TravelogEntity>> findAllByMainPlaceContainingOrderByLikesDesc(String mainPlace);

}
