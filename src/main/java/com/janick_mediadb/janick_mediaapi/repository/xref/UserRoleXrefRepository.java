package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UserRoleXrefEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface UserRoleXrefRepository extends JpaRepository<UserRoleXrefEntity, Long> {

    @Query("SELECT ur.user FROM UserRoleXrefEntity ur WHERE ur.role.id = :roleId")
    LinkedList<UsersEntity> findUsersByRole(int roleId);

    @Query("SELECT ur.role FROM UserRoleXrefEntity ur WHERE ur.user.id = :userId")
    LinkedList<RoleEntity>  findRolesByUserId(int userId);

    @Query("SELECT ur FROM UserRoleXrefEntity ur WHERE ur.user.id = :userId")
    LinkedList<UserRoleXrefEntity> findRoleReferencesByUserId(int userId);
}
