package com.kakaogroom6.server.member.repository;

import com.kakaogroom6.server.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long > {
    Optional<MemberEntity> findByEmail(String email);
}
