package com.kakaogroom6.server.member.repository;

import com.kakaogroom6.server.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MemberEntity, Long > {
    Optional<MemberEntity> findByName(String name);
}
