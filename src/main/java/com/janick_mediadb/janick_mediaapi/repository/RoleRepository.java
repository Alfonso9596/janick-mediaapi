package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(String name);
}
