package com.kakaogroom6.server.domain.member.repository;

import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long > {
    Optional<MemberEntity> findByEmail(String email);
}
