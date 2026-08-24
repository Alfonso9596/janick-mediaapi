package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.security.RoleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(String name);

    @Query("SELECT r FROM RoleEntity r ORDER BY name")
    List<RoleEntity> findAllOrderByName();
}
