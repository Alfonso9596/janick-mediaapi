package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UsersEntity, Integer>, JpaSpecificationExecutor<UsersEntity> {

    Optional<UsersEntity> findByUsername(String username);

    Boolean existsByUsername(String username);
}
