package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UsersEntity, Integer> {

    Optional<UsersEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
