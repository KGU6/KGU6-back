package com.kakaogroom6.server.domain.keword.repository;

import com.kakaogroom6.server.domain.keword.entity.KewordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KewordRepository extends JpaRepository<KewordEntity, Long> {
    List<KewordEntity> findByTravelogEntityId(Long travelogId);

}
