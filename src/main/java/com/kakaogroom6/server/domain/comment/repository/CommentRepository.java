package com.kakaogroom6.server.domain.comment.repository;

import com.kakaogroom6.server.domain.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<List<CommentEntity>> findAllByTravelogId(Long travelogId);
}
