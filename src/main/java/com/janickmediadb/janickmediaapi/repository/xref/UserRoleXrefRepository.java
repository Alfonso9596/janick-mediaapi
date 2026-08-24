package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.security.RoleEntity;
import com.janickmediadb.janickmediaapi.entity.security.UserRoleXrefEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import java.util.LinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRoleXrefRepository extends JpaRepository<UserRoleXrefEntity, Long> {

    @Query("SELECT ur.user FROM UserRoleXrefEntity ur WHERE ur.role.id = :roleId")
    LinkedList<UsersEntity> findUsersByRole(int roleId);

    @Query("SELECT ur.role FROM UserRoleXrefEntity ur WHERE ur.user.id = :userId")
    LinkedList<RoleEntity> findRolesByUserId(int userId);

    @Query("SELECT ur FROM UserRoleXrefEntity ur WHERE ur.user.id = :userId")
    LinkedList<UserRoleXrefEntity> findRoleReferencesByUserId(int userId);
}
