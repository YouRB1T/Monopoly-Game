package com.monopoly.repository;

import com.monopoly.domain.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerEntityRepository extends JpaRepository<PlayerEntity, UUID> {
    Optional<PlayerEntity> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}