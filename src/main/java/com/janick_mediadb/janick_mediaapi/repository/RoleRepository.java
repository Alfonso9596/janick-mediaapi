package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.security.ERole;
import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(ERole name);

    @Query("SELECT r FROM RoleEntity r ORDER BY name")
    List<RoleEntity> findAllOrderByName();
}
